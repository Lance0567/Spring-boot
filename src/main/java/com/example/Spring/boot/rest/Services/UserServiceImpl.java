package com.example.Spring.boot.rest.Services;

import com.example.Spring.boot.rest.Domain.User;
import com.example.Spring.boot.rest.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
   UserRepository userRepository;

    @Override
    public User validation(String email, String password) throws EtAutException {
        return null;
    }

    @Override
    public User registerUser(String email, String username, String password) throws EtAutException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null) email = email.toLowerCase();
        if (!pattern.matcher(email).matches())
            throw new EtAutException("Invalid email format");
        Integer count = userRepository.getCountByEmail(email);
        if(count > 0)
            throw new EtAutException("Email already in use");
        Integer userId = userRepository.create(email, username, password);
        return userRepository.findyById(userId);
    }
}
