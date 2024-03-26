package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.*;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    /**
     * Handler to register a new account.
     * @param account - the new account that needs to be registered.
     * @return on success - an Account object wrapped in a ResponseEntity as the response body to the HTTP caller.
     * @return on failure due to duplicate username - returns a ResponseEntity with a status of HttpStatus.CONFLICT (409).
     * @return on failure due to invalid account - returns a ResponseEntity with a status of HttpStatus.BAD_REQUEST (400).
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        try 
        {
            Account response = accountService.registerAccount(account);
            // return the account in the response body if all goes well 
            return ResponseEntity.ok(response);
        } 
        catch (DuplicateUsernameException e) 
        {
            // return error code 409 if a duplicate account exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } 
        catch (InvalidAccountException e)
        {
            // return error code 400 for all other errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Handler to login a user.
     * @param account - the account that needs to be logged in.
     * @return on succcess - an Account object wrapped in a ResponseEntity as the response body to the HTTP caller.
     * @return on failure due to unauthorized login details - returns a ResponseEntity with a status of HttpStatus.UNAUTHORIZED (401).
     */              
    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account) 
    {
        try
        {
            Account response = accountService.loginAccount(account);
            return ResponseEntity.ok(response);
        } 
        catch (UnauthorizedException e) 
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Handler to post a new message.
     * @param message - the message that needs to be posted.
     * @return on success - a Message object wrapped in a ResponseEntity as the response body to the HTTP caller.
     * @return on failure due to invalid message - returns a ResponseEntity with a status of HttpStatus.BAD_REQUEST (400).
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        try
        {
            Message savedMessage = messageService.submitMessage(message);
            return ResponseEntity.ok(savedMessage);
        } 
        catch (InvalidMessageException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Handler to get all messages.
     * @return a list of Message objects wrapped in a ResponseEntity as the response body to the HTTP caller.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.ok(allMessages);
    }

    /**
     * Handler to get a specific message by its id.
     * @param message_id - the id of the message to be retrieved.
     * @return the Message object with the given id wrapped in a ResponseEntity as the response body to the HTTP caller.
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.ok(message);
    }

    /**
     * Handler to delete a specific message by its id.
     * @param message_id - the id of the message to be deleted.
     * @return If no rows were affected - an empty ResponseEntity with a status of HttpStatus.OK (200).
     * @return If the message was successfully deleted - the number of rows affected wrapped in a ResponseEntity as the response body to the HTTP caller.
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessageById(@PathVariable Integer message_id) {
        long rowsAffected = messageService.deleteMessageById(message_id);
        if (rowsAffected == 0) 
        {
            return ResponseEntity.ok().build();
        } 
        else 
        {
            return ResponseEntity.ok(rowsAffected);
        }
    }

    /**
     * Handler to update a specific message by its id.
     * @param message_id - the id of the message to be updated.
     * @param message - the new content for the message.
     * @return on successful update - a ResponseEntity with a status of HttpStatus.OK (200) and a body of 1.
     * @return on failed update - a ResponseEntity with a status of HttpStatus.BAD_REQUEST (400).
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessageByIdHandler(@PathVariable Integer message_id, @RequestBody Message message) {
        // update the id first just in case
        // the request body is not guaranteed to contain the actual id (only the param)
        message.setMessage_id(message_id);
        // Update the message with the new content
        Message updatedMessage = messageService.updateMessageById(message);
        if (updatedMessage != null) {
            return ResponseEntity.ok(1);
        } else {
            // 400 if the message is not updated
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handler to get all messages posted by a specific account.
     * @param account_id The id of the account whose messages are to be retrieved.
     * @return A list of Message objects posted by the account, wrapped in a ResponseEntity as the response body to the HTTP caller.
     */
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer account_id) {
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        return ResponseEntity.ok(messages);
    }
}

