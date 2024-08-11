package com.example.scheduler.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    public KafkaTemplate<String,String> kafkaTemplate;

    @Value("${kafka.topic.name}")
    public String topicName;

    private Logger logger = LoggerFactory.getLogger(KafkaService.class);

    public boolean sendRequest(String key, String data){
        kafkaTemplate.send(topicName,key,data);
        logger.info("Request sent for key="+key);
        return true;
    }
}
