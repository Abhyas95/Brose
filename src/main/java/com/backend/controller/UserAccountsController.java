package com.backend.controller;

import com.backend.Model.MongoDbTable.UserAccounts;
import com.backend.repository.MongoRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class UserAccountsController {
    @Autowired
    private UserRepository userRepository;

    // Create a new user
    @PostMapping
    public UserAccounts createUser(@RequestBody UserAccounts user) {
        return userRepository.save(user);
    }


}
