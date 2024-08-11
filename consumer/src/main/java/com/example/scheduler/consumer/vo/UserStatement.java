package com.example.scheduler.consumer.vo;

import com.example.scheduler.consumer.model.Transaction;

import java.util.List;

public class UserStatement {

    private Integer userId;
    private List<Transaction> transactions;

    public UserStatement() {
    }

    public UserStatement(Integer userId, List<Transaction> transactions) {
        this.userId = userId;
        this.transactions = transactions;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "UserStatement{" +
                "userId=" + userId +
                ", transactions=" + transactions +
                '}';
    }
}
