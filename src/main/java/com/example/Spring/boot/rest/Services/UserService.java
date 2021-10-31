package com.example.Spring.boot.rest.Services;

import com.example.Spring.boot.rest.Domain.User;

public interface UserService {

    User validation(String email, String password) throws EtAutException;

    User registerUser(String email, String username, String password) throws EtAutException;

    class EtAutException extends Exception {
        public EtAutException(String invalid_email_format) {

        }
    }
}
