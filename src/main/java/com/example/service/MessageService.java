package com.example.service;

import com.example.entity.Message;
import com.example.exception.*;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;


    public Message createMessage(Message message) {
        // Validate message data
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) 
        {
            throw new InvalidMessageException("Invalid message data");
        }

        // Validate message writer
        if(!accountRepository.findByPostedBy(message.getPosted_by()).isPresent())
        {
            throw new InvalidMessageException("Unrecognized poster");
        }
        // Save the message to the database
        return messageRepository.save(message);
    }
}
