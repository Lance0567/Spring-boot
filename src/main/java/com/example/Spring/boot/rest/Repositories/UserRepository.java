package com.example.Spring.boot.rest.Repositories;

import com.example.Spring.boot.rest.Domain.User;
import com.example.Spring.boot.rest.Exceptions.EtAuthException;

public interface UserRepository {

    Integer create(String email, String username, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    User findyById(Integer userId);

    Integer getCountByEmail(String email);
}
