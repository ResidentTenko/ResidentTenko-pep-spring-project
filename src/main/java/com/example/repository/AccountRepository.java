package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

    /**
     * @param username the username of the account.
     * @return the account that matches the username
     */
    Optional<Account> findByUsername(String username);

    /**
     * @param postedById the id of the message poster
     * @return the account that matches the poster id
     */
    @Query("FROM Account WHERE account_id = :postedByIdVar")
    Optional<Account> findByPostedBy(@Param("postedByIdVar") Integer postedById);
}
