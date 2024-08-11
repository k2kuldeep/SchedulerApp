package com.example.scheduler.consumer.config;

import com.example.scheduler.consumer.dao.UserRepository;
import com.example.scheduler.consumer.model.RequestFor;
import com.example.scheduler.consumer.model.Users;
import com.example.scheduler.consumer.service.StatementGenerateService;
import com.example.scheduler.consumer.vo.UserStatement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class KafkaConfiguration {

    public static final String topic = "";
    public static final String group = "";

    @Autowired
    private UserRepository userRepository;

    @Value("${kafka.topic.name}")
    public String topicName;

    @Value("${spring.kafka.consumer.group-id}")
    public String groupId;

    @Autowired
    public StatementGenerateService cardStatementGenerateService;

    Logger logger = LoggerFactory.getLogger(KafkaConfiguration.class);
    public static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "scheduler" ,groupId = "group-1")
    public void listener(ConsumerRecord<String,String> record){
        if(record.key().equalsIgnoreCase(RequestFor.CARD_STATEMENT)){
            List<UserStatement> userStatements = new ArrayList<>();
            try {
                userStatements = objectMapper.reader().forType(new TypeReference<List<UserStatement>>(){}).readValue(record.value());
            } catch (JsonProcessingException e) {
                logger.error("key="+record.key()+" : value="+record.value() + " Error:"+e);
            }
            for(UserStatement userStatement : userStatements){
                Users user = userRepository.getUsersByUserId(userStatement.getUserId());
                logger.info(user.getEmailId());
                logger.info(userStatement.toString());
                String  cardStatementFilePath = cardStatementGenerateService.generate(user,userStatement);
                if(cardStatementFilePath != null){
                    logger.info(cardStatementFilePath);
                }
            }
        }


    }



}
