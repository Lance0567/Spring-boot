package com.example.midterm2.Controller;

import com.example.midterm2.Entity.AuthenticationRequest;
import com.example.midterm2.Entity.User;
import com.example.midterm2.Exceptions.ResourceNotFoundException;
import com.example.midterm2.JWT.AuthenticationResponse;
import com.example.midterm2.JWT.JwtUtil;
import com.example.midterm2.Repositories.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.midterm2.Service.MyUserDetailsService;
import com.example.midterm2.Status.Status;
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
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;


    // Get users
    @GetMapping("/customer")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    // Get user by id
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
                                        "User not found for this id :: " + userId
                                )
                );
        return ResponseEntity.ok().body(user);
    }

    // Save user
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e){
                throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
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
                                        "User not found for this id :: " + userId
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
