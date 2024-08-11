package com.example.scheduler.producer.service;


import com.example.scheduler.producer.dao.UserRepository;
import com.example.scheduler.producer.model.RequestFor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardStatementScheduler {

    private Logger logger = LoggerFactory.getLogger(CardStatementScheduler.class.getName());

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public KafkaService kafkaService;

    @Value("${fetch.users.page.size}")
    private Integer userPerPageSize;

    @Autowired
    private CardStatementService cardStatementService;

    @Scheduled(cron = "0 0 1 * * ?") // Cron expression for 1:00 AM every day
    public void sendCardStatementRequestToKafka() {
        LocalDate currentDate = LocalDate.now();
        Integer usersCount = userRepository.countOfUsersWithExecuteDateEqualsCurrentDate(currentDate);
        int pages = (usersCount+userPerPageSize-1) /userPerPageSize;
        ObjectMapper objectMapper = new ObjectMapper();
        for(int page=0;page<pages;page++){
            String responseString = null;
            try {
                responseString = objectMapper.writeValueAsString(cardStatementService.getCardStatements(currentDate,page,userPerPageSize));
            } catch (JsonProcessingException e) {
                logger.error("Exception while converting user statement to json: "+e);
                continue;
            }
            kafkaService.sendRequest(RequestFor.CARD_STATEMENT, responseString);
        }
    }
}
