package com.example.scheduler.producer.service;

import com.example.scheduler.producer.dao.UserRepository;
import com.example.scheduler.producer.model.Users;
import com.example.scheduler.producer.vo.UserStatement;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardStatementService {

    private Logger logger = LoggerFactory.getLogger(CardStatementService.class.getName());

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public FetchUserTransaction fetchUserTransaction;

    public List<UserStatement> getCardStatements(LocalDate date,int page, int pageSize){
        List<UserStatement> userStatements = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize);
        for (Long userId : userRepository.findUsersWithExecuteDateEqualsCurrentDate(date,pageable)){
            UserStatement userStatement = new UserStatement(userId,fetchUserTransaction.getUserStatement(userId, LocalDateTime.now()));
            userStatements.add(userStatement);
        }
        return userStatements;
    }
}
