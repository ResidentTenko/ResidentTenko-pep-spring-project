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
     * Registers a new account.
     * @param account - the account to be registered.
     * @return the registered account.
     * @throws InvalidAccountException If the account details are invalid (e.g., the username is blank or the password is less than 4 characters long).
     * @throws DuplicateUsernameException If the username is already taken.
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

        // Save the new account in the database
        return accountRepository.save(account);
    }

    /**
     * Logs in an account.
     * @param account - the account to be logged in.
     * @return the logged in account.
     * @throws UnauthorizedException If the username or password is invalid.
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