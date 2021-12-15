package com.example.midterm2.Controller;

import com.example.midterm2.Entity.JwtResponse;
import com.example.midterm2.Entity.User;
import com.example.midterm2.Exceptions.ResourceNotFoundException;
import com.example.midterm2.Repositories.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.midterm2.Services.MyUserDetailsService;
import com.example.midterm2.Status.Status;
import com.example.midterm2.Utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    //get users
    @GetMapping("/customer")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    //get user by id
    @GetMapping("/customer/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable(value = "id") Long userId
    )
            throws ResourceNotFoundException {
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Employee not found for this id :: " + userId
                                )
                );
        return ResponseEntity.ok().body(user);
    }

    // authenticate user
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody User user) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        final String token = jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

    // save user
    @PostMapping("/customer/register")
    public Status registerUser(@RequestBody User user) {
        userRepository.save(user);
        return Status.Registered_Successfully;
    }

    // login user
    @PostMapping("/customer/login")
    public Status loginUser(@Valid @RequestBody User user) {
        List<User> users = userRepository.findAll();
        for (User login : users) {
            if (login.equals(user)) {
                return Status.Successful_Login;
            }
        }        return Status.Invalid_Details;
    }

    // logout user
    @PostMapping("/customer/logout")
    public Status logoutUser(@Valid @RequestBody User user) {
        List<User> users = userRepository.findAll();
        for (User logout : users) {
            if (logout.equals(user)) {
                return Status.Logout_Successful;
            }
        }   return Status.Invalid_Details;
    }

    //update user
    @PutMapping("/customer/update/{id}")
    public Status updateUser(
            @PathVariable(value = "id") Long userId,
            @Validated @RequestBody User userDetails
    )
            throws ResourceNotFoundException {
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Employee not found for this id :: " + userId
                                )
                );

        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        final User updatedUser = userRepository.save(user);
        ResponseEntity.ok(updatedUser);
        return Status.Updated_Successfully;
    }

    //delete user
    @DeleteMapping("/customer/delete/{userId}")
    public Map<String, Boolean> deleteUser(
            @PathVariable(value = "userId") Long userId
    )
            throws ResourceNotFoundException {
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "User not found for this id :: " + userId
                                )
                );
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Message : Deleted Successfully", Boolean.TRUE);
        return response;
    }

    // delete all users
    @DeleteMapping("/customers/deleteAll")
    public Status deleteUsers() {
        userRepository.deleteAll();
        return Status.Successfully_Deleted;
    }
}
