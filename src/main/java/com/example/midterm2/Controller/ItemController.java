package com.example.midterm2.Controller;

import com.example.midterm2.Entity.Item;
import com.example.midterm2.Exceptions.ResourceNotFoundException;
import com.example.midterm2.Repositories.ItemRepository;
import com.example.midterm2.Repositories.UserRepository;
import com.example.midterm2.Status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
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

    @PostMapping("/customer/{userId}/items")
    public Item createItem(@PathVariable (value = "userId") Long userId,
                            @Validated @RequestBody Item item) throws ResourceNotFoundException {
        return userRepository.findById(userId).map(customer -> {
            item.setCustomer(customer);
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResourceNotFoundException("Customer id " + userId + " not found"));
    }

    @PutMapping("/customer/{userId}/items/{itemId}")
    public Item updateItem(@PathVariable (value = "userId") Long customerId,
                           @PathVariable (value = "itemId") Long itemId,
                           @Validated @RequestBody Item itemRequest) throws ResourceNotFoundException {
        if(!userRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Item id " + customerId + " not found");
        }

        return itemRepository.findById(itemId).map(item -> {
            item.setItem_name(itemRequest.getItem_name());
            item.setCategory_name(itemRequest.getCategory_name());
            item.setPrice(itemRequest.getPrice());
            System.out.println("Item Successfully updated");
            return itemRepository.save(item);
        }).orElseThrow(() -> new ResourceNotFoundException("User id " + itemId + "not found"));
    }


    @DeleteMapping(value = "/customer/{userid}/items/{itemId}")
    public Map<String, Boolean> deleteItem(
            @PathVariable(value = "userid") Long itemId
    )
            throws ResourceNotFoundException {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Item not found for this id :: " + itemId
                                )
                );
        itemRepository.delete(item);
        Map<String, Boolean> response2 = new HashMap<>();
        response2.put("Message : Deleted Successfully", Boolean.TRUE);
        return response2;
    }

    @DeleteMapping("/customer/all/deleteAllItems")
    public Status deleteItems() {
        itemRepository.deleteAll();
        return Status.Successfully_Deleted;
    }
}
