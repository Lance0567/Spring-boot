package com.example.Spring.boot.rest.Repositories;

import com.example.Spring.boot.rest.Domain.User;
import com.example.Spring.boot.rest.Exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID, EMAIL, USERNAME," +
            " PASSWORD) VALUES(NEXTVAL('ET_USERS_SEQ'), ?, ?, ?, ?)";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Integer create(String email, String username, String password) throws EtAuthException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement
            }, keyHolder);
        }catch (Exception e)
            throw new EtAuthException("Invalid details, Failed to create account");
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        return null;
    }

    @Override
    public User findyById(Integer userId) {
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        return null;
    }
}
