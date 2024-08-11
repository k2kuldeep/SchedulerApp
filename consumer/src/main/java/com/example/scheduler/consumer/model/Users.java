package com.example.scheduler.consumer.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class Users extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userId_generator")
    @SequenceGenerator(name="userId_generator", sequenceName = "userId_seq",initialValue = 10000)
    private Long userId;
    private String userName;
    private Integer accountNumber;
    private String emailId;
    private LocalDate executeDate;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Transaction> transaction;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }

    public void addTransaction(Transaction transaction){
        if(this.transaction == null){
            this.transaction=new ArrayList<>();
        }
        this.transaction.add(transaction);
    }

    public LocalDate getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(LocalDate executeDate) {
        this.executeDate = executeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users user = (Users) o;

        if (!Objects.equals(userId, user.userId)) return false;
        if (!Objects.equals(userName, user.userName)) return false;
        if (!Objects.equals(accountNumber, user.accountNumber))
            return false;
        if (!Objects.equals(emailId, user.emailId)) return false;
        return Objects.equals(transaction, user.transaction);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        result = 31 * result + (emailId != null ? emailId.hashCode() : 0);
        result = 31 * result + (transaction != null ? transaction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", accountNumber=" + accountNumber +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}
