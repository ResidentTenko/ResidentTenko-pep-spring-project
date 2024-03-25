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
    * @param account The new account that needs to be registered.
    * @return on succcess: An Account object wrapped in a ResponseEntity as the response body to the HTTP caller.
    * @return on failure: returns UnauthorizedException
    */
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        try 
        {
            Account response = accountService.registerAccount(account);
            return ResponseEntity.ok(response);
        } 
        catch (DuplicateUsernameException e) 
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } 
        catch (InvalidAccountException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
    * Handler for user logins.
    * @param account The new account that needs to be registered.
    * @return on succcess: An Account object wrapped in a ResponseEntity as the response body to the HTTP caller.
    * @return on failure: returns UnauthorizedException
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

    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        try {
            Message savedMessage = messageService.submitMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (InvalidMessageException e) {
            // Handle validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.ok(allMessages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.ok(message);
    }

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

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer account_id) {
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        return ResponseEntity.ok(messages);
    }
}

