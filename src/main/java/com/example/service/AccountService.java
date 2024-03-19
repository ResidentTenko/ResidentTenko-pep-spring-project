package com.example.service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidAccountException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // In AccountService.java

    public Account registerAccount(Account account) throws DuplicateUsernameException, InvalidAccountException {
        // Check if the username is not blank and the password is at least 4 characters long
        if (account.getUsername() == null || account.getUsername().trim().isEmpty() || account.getPassword().length() < 4) {
            throw new InvalidAccountException("Invalid account details");
        }

        // Check if the account already exists based on username
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            // Throw an exception if the username is already taken
            throw new DuplicateUsernameException("Username already exists");
        }

        // Save the new account in the database since it does not exist
        return accountRepository.save(account);
    }
    
}