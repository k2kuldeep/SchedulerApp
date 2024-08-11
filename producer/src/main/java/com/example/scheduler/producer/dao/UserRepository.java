package com.example.scheduler.producer.dao;


import com.example.scheduler.producer.model.Transaction;
import com.example.scheduler.producer.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("select users.id from Users users where users.executeDate = :currentDate")
    Page<Long> findUsersWithExecuteDateEqualsCurrentDate(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("select count(*) from Users users where users.executeDate = :currentDate")
    Integer countOfUsersWithExecuteDateEqualsCurrentDate(@Param("currentDate") LocalDate currentDate);

    Users getUsersByUserId(Long userId);

    @Query("select tnx from Users users inner join users.transaction tnx where users.userId=:userId AND tnx.transactionDate <:currentDate AND tnx.transactionDate > :endDate")
    public List<Transaction> getUserTransactionByDate(@Param("userId") Long userId, @Param("currentDate") LocalDateTime currentDate, @Param("endDate") LocalDateTime endDate);

}
