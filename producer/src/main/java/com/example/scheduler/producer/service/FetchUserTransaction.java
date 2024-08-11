package com.example.scheduler.producer.service;

import com.example.scheduler.producer.dao.TransactionRepository;
import com.example.scheduler.producer.dao.UserRepository;
import com.example.scheduler.producer.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FetchUserTransaction {

    @Autowired
    public UserRepository userRepository;

    public List<Transaction> getUserStatement(Long userId, LocalDateTime localDateTime){
        return userRepository.getUserTransactionByDate(userId,localDateTime,localDateTime.minusMonths(1l));
    }
}
