package com.example.Spring.boot.Repositories;

import com.example.Spring.boot.Entity.User;
import com.example.Spring.boot.Exceptions.EtAuthException;

public interface UserRepository {

    Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws  EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
