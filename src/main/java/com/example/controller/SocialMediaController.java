package com.example.controller;

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

}

