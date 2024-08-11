package com.example.scheduler.consumer.dao;



import com.example.scheduler.consumer.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users getUsersByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query("update Users set executeDate=:newExecutionDate where id = :userId")
    void updateUserExecuteDate(@Param("newExecutionDate") LocalDate newExecutionDate, @Param("userId") Long userId);

}
