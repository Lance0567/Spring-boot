package com.example.midterm2.Controller;

import com.example.midterm2.Entity.User;
import com.example.midterm2.Exepception.ResourceNotFoundException;
import com.example.midterm2.Repositories.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.midterm2.Status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

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

    //save user
    @PostMapping("/customer/register")
    public Status registerUser(@RequestBody User user) {
        userRepository.save(user);
        return Status.Registered_Successfully;
    }

    // login user
    @PostMapping("/customer/login")
    public Status loginCustomers(@Valid @RequestBody User users) {
        List<User> user = userRepository.findAll();
        for (User other : user) {
            if (other.equals(users)) {
                return Status.Successful_Login;
            }
        }        return Status.Invalid_Details;
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
    @DeleteMapping("/customer/deleteAll/{id}")
    public Map<String, Boolean> deleteUser(
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
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Message : Deleted Successfully", Boolean.TRUE);
        return response;
    }
}
