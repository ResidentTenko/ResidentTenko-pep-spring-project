package com.example.service;

import com.example.entity.Message;
import com.example.exception.*;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;


    public Message submitMessage(Message message) {
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

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }else{
            return null;
        }
    }

    public long deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)) 
        {
            messageRepository.deleteById(messageId);
            return 1;
        } 
        else 
        {
            return 0;
        }
    }

    /** public void deleteStore(long id){
        storeRepository.deleteById(id);
    } */
}
