package com.example.midterm2.Controller;

import com.example.midterm2.Entity.Item;
import com.example.midterm2.Entity.User;
import com.example.midterm2.Exepception.ResourceNotFoundException;
import com.example.midterm2.Repositories.ItemRepository;
import com.example.midterm2.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/customer/all/items")
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/customer/{id}/items")
    public List<Item> getAllCommentsByPostId(@PathVariable (value = "id") Long id) {
        return itemRepository.findByUserId(id);
    }

    @PostMapping("/customer/{customerid}/items")
    public Item createItem(@PathVariable (value = "userId") Long userId,
                           @Valid @RequestBody Item item) throws ResourceNotFoundException {
        return userRepository.findById(userId).map(user -> {
            item.setCustomer(user);
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer id " + userId + " not found"));
    }

    @PutMapping("/customer/{customerid}/items/{itemid}")
    public Item updateItem(@PathVariable (value = "customerid") Long customerid,
                           @PathVariable (value = "itemid") Long itemid,
                           @Valid @RequestBody Item itemRequest) throws ResourceNotFoundException {
        if(!userRepository.existsById(customerid)) {
            throw new ResourceNotFoundException("Item id " + customerid + " not found");
        }

        return itemRepository.findById(itemid).map(item -> {
            item.setItem_name(itemRequest.getItem_name());
            item.setCategory_name(itemRequest.getCategory_name());
            item.setPrice(itemRequest.getPrice());
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer id " + itemid + "not found"));
    }


    @DeleteMapping(value = "/customer/{customerid}/items/{itemid}")
    public Map<String, Boolean> deleteUser(
            @PathVariable(value = "id") Long itemid
    )
            throws ResourceNotFoundException {
        User item = userRepository
                .findById(itemid)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Employee not found for this id :: " + itemid
                                )
                );
        userRepository.delete(item);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Message : Deleted Successfully", Boolean.TRUE);
        return response;
    }
}
