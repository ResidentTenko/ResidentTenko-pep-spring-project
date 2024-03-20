package com.example.service;

import com.example.entity.Account;
import com.example.exception.*;
import com.example.repository.AccountRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    
    /**
     * Use the AccountRepository to persist an Account. The given Account will not have an id provided.
     * Method should check:
     * if the username is not blank, 
     * the password is at least 4 characters long, 
     * and an Account with that username does not already exist
     * @param account - an Account object that needs to be registered.
     * @return Account if the persisted account was successful
     */
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

    /**
     * Use the AccountRepository to login an Account.
     * @param account - an Account object trying to login
     * @return on success - Account object if login is sucessful (it exists in the database)
     */
    public Account loginAccount(Account account) {
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());

        if (existingAccount.isPresent() && existingAccount.get().getPassword().equals(account.getPassword())) {
            return existingAccount.get();
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }
    
}