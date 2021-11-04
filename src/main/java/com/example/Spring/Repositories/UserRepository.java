package com.example.Spring.Repositories;

import com.example.Spring.Controller.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository {

    Optional<User> findByEmail(String email);
}
