package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    Optional<Account> findByUsername(String username);

    @Query("FROM Account WHERE account_id = :postedByIdVar")
    Optional<Account> findByPostedBy(@Param("postedByIdVar") Integer postedById);
}
