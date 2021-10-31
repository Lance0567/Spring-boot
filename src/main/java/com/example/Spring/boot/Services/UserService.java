package com.example.Spring.boot.Services;

import com.example.Spring.boot.Entity.User;
import com.example.Spring.boot.Exceptions.EtAuthException;

public interface UserService {

    User validateUser(String email, String password) throws EtAuthException;

    User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;

}
