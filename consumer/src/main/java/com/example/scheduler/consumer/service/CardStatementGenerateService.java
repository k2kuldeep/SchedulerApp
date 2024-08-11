package com.example.scheduler.consumer.service;

import com.example.scheduler.consumer.dao.UserRepository;
import com.example.scheduler.consumer.model.Transaction;
import com.example.scheduler.consumer.model.Users;
import com.example.scheduler.consumer.vo.UserStatement;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardStatementGenerateService implements StatementGenerateService{

    private Logger logger = LoggerFactory.getLogger(CardStatementGenerateService.class);

    @Value("${card.statement.template.file}")
    public String cardStatementTemplate;

    @Value("${customer.card.statement.file.path}")
    public String customerStatementFilePath;

    public static final String CARD_STATEMENT = "CARD_STATEMENT_";

    @Autowired
    public TemplateService templateService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String generate(Users user, UserStatement userStatement) {
        Path outputFilePath = Paths.get(customerStatementFilePath.concat("\\").concat(CARD_STATEMENT).concat(user.getUserId().toString()).concat("_").concat(user.getExecuteDate().getMonth().toString()).concat(".pdf"));
        File file = new File(outputFilePath.getParent().toString());
        if(!file.exists()){
            file.mkdirs();
        }
        String outputFile = outputFilePath.toFile().getAbsolutePath();
        Map<String ,Object> data = new HashMap<>();
        FieldsMetadata metadata = new FieldsMetadata();
        data.put("userName", user.getUserName());
        data.put("accountNumber",user.getAccountNumber().toString());
        data.put("emailId",user.getEmailId());
        data.put("month",user.getExecuteDate().getMonth().toString());
        //
        List<Map<String,Object>> transactionList = new ArrayList<>();
        for (Transaction transaction : userStatement.getTransactions()){
            Map<String,Object> transactionMap = new HashMap<>();
            transactionMap.put("amount",transaction.getAmount().doubleValue());
            transactionMap.put("status",transaction.getStatus().toString());
            transactionMap.put("transactionDate",transaction.getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            transactionMap.put("transactionType",transaction.getTransactionType().toString());
            transactionList.add(transactionMap);
        }
        data.put("transactionList",transactionList);
        metadata.addFieldAsList("transactionList.amount");
        metadata.addFieldAsList("transactionList.status");
        metadata.addFieldAsList("transactionList.transactionDate");
        metadata.addFieldAsList("transactionList.transactionType");
        try {
            templateService.generateFile(cardStatementTemplate, outputFile, data, metadata);
        } catch (Exception e) {
            logger.error("Exception while generating statement file :"+outputFile);
            return null;
        }
        userRepository.updateUserExecuteDate(user.getExecuteDate().plusMonths(1),user.getUserId());
        return outputFile;
    }
}
