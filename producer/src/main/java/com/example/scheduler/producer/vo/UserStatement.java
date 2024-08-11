package com.example.scheduler.producer.vo;



import com.example.scheduler.producer.model.Transaction;

import java.util.List;

public class UserStatement {

    private Long userId;
    private List<Transaction> transactions;

    public Long getUserId() {
        return userId;
    }

    public UserStatement(){

    }

    public UserStatement(Long userId, List<Transaction> transactions) {
        this.userId = userId;
        this.transactions = transactions;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
