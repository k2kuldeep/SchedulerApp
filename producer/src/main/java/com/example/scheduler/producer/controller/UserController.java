package com.example.scheduler.producer.controller;

import com.example.scheduler.producer.dao.TransactionRepository;
import com.example.scheduler.producer.dao.UserRepository;
import com.example.scheduler.producer.model.RequestFor;
import com.example.scheduler.producer.model.Transaction;
import com.example.scheduler.producer.model.Users;
import com.example.scheduler.producer.model.enums.TransactionStatus;
import com.example.scheduler.producer.model.enums.TransactionType;
import com.example.scheduler.producer.service.CardStatementService;
import com.example.scheduler.producer.service.FetchUserTransaction;
import com.example.scheduler.producer.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

//temp controller
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    public KafkaService kafkaService;

    @Autowired
    private FetchUserTransaction fetchUserTransaction;

    @Value("${fetch.users.page.size}")
    private Integer userPerPageSize;

    @Autowired
    private CardStatementService cardStatementService;

    private Logger logger = LoggerFactory.getLogger(UserController.class.getName());

    @GetMapping("/insertUsers/{num}")
    public boolean insertUsers(@PathVariable("num") int num){
        logger.info("users added in db");
        Random random = new Random(1234567);
        for (int i=0;i<num;i++){
            Users user = new Users();
            user.setUserName(UUID.randomUUID().toString().substring(0,12));
            user.setAccountNumber(random.nextInt(100000,1000000));
            user.setEmailId(UUID.randomUUID().toString().substring(0,10).concat("@email.com"));
            user.setExecuteDate(LocalDateTime.now().toLocalDate());
            userRepository.save(user);
        }
        return true;
    }

    @GetMapping("/doTransaction")
    public boolean doTransactionOfUser(@RequestParam("userId") Long userId){
        Users user = userRepository.getUsersByUserId(userId);
        if (user==null){
            logger.info("userId: "+userId+ " not found");
            return false;
        }
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDateTime.now().minusDays(5));
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setAmount(BigDecimal.valueOf(Math.random()*1000));
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        user.addTransaction(transaction);
        userRepository.save(user);
        logger.info("transaction added for user:"+userId);
        return true;
    }

    @GetMapping("/allUsers")
    public List<Users> getAllUsers(){
        logger.info("All users fetched");
        return userRepository.findAll();
    }

    //send credit card statement to kafka
    @GetMapping("/sendCardStatementRequestToKafka")
    public boolean sendRequestKafka(){
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
        return true;
    }
}
