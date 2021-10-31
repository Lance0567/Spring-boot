package com.example.Spring.boot.rest.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        return email + ", " + username + ", " + password;
    }
}
