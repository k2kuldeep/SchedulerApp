package com.example.scheduler.consumer.service;

import com.example.scheduler.consumer.model.Users;
import com.example.scheduler.consumer.vo.UserStatement;


/**
 * The interface Statement generate service.
 */
public interface StatementGenerateService {


    /**
     * Generate statement file.
     *
     * @param user          the user
     * @param userStatement the user statement
     * @return the string path of statement file. return null if output file not created
     */
    String generate(Users user, UserStatement userStatement);
}
