package com.example.midterm2.Controller;

import com.example.midterm2.Entity.User;
import com.example.midterm2.Exepception.ResourceNotFoundException;
import com.example.midterm2.Repositories.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/customer")
    public User registerUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }

    //update user
    @PutMapping("/customer/{id}")
    public ResponseEntity<User> updateUser(
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
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());
        user.setPassword(userDetails.getPassword());
        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    //delete user
    @DeleteMapping("/customer/{id}")
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
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
