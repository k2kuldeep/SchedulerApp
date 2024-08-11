package com.example.scheduler.producer.dao;


import com.example.scheduler.producer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select tnx from Users users inner join users.transaction tnx where users.id=:userId AND tnx.transactionDate BETWEEN :currentDate AND :endDate")
    public List<Transaction> getUserTransactionByDate(@Param("userId") String userId,@Param("currentDate") LocalDate currentDate, @Param("endDate") LocalDate endDate);

}
