package com.example.midterm2.Repositories;

import com.example.midterm2.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    void authenticate(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken);

    Object getUsername();

    Object getPassword();


}
