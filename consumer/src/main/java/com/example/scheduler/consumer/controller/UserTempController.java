package com.example.scheduler.consumer.controller;


import com.example.scheduler.consumer.dao.UserRepository;
import com.example.scheduler.consumer.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserTempController {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(UserTempController.class.getName());

    @GetMapping("/allUsers")
    public List<Users> getAllUsers(){
        logger.info("All users fetched");
        return userRepository.findAll();
    }
}
