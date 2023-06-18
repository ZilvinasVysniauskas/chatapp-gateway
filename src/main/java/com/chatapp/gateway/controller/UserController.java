package com.chatapp.gateway.controller;

import com.chatapp.gateway.security.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public String getUser(@CurrentUser String userId) {
        return "Hello! " + userId;
    }
}
