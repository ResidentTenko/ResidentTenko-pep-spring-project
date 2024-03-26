package com.example.service;

import com.example.entity.Message;
import com.example.exception.*;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Submits a new message.
     * @param message The message to be submitted.
     * @return The submitted message.
     * @throws InvalidMessageException If the message data is invalid (e.g., the message text is null, empty, or longer than 255 characters) or if the poster is unrecognized.
     */
    public Message submitMessage(Message message) {
        // Validate message data
        if (message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) 
        {
            throw new InvalidMessageException("Invalid message data");
        }

        // Validate message poster
        if(!accountRepository.findByPostedBy(message.getPosted_by()).isPresent())
        {
            throw new InvalidMessageException("Unrecognized poster");
        }
        // Save the message to the database
        return messageRepository.save(message);
    }

    /**
     * Retrieves all messages.
     * @return a list of all messages.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Retrieves a specific message by its id.
     * @param messageId - the id of the message to be retrieved.
     * @return The message with the given id, or null if no such message exists.
     */
    public Message getMessageById(Integer messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }else{
            return null;
        }
    }

    /**
     * Deletes a specific message by its id.
     * @param messageId - the id of the message to be deleted.
     * @return on success - the number of rows affected (1).
     * @return on failure - the number of rows affected(0).
     */
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

    /**
     * Updates the text of a specific message by its id.
     * @param message The message with the new text to be updated.
     * @return on successful update - the updated message, 
     * @return on failure (or invalid message text) - null.
     */
    @Transactional
    public Message updateMessageById(Message message) {
        // get the message that matches the id of the message param
        Message databaseMessage = getMessageById(message.getMessage_id());

        // if the message doesn't exist return null
        if(databaseMessage == null) {
            return null;
        }

        // Check if the message is blank or is the right length
        if (message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        // if the message is valid then update the text of the database message
        databaseMessage.setMessage_text(message.getMessage_text());

        // save the updated message into the database
        messageRepository.save(databaseMessage);

        // return the updated message
        return databaseMessage;
    }

    /**
     * Retrieves all messages posted by a specific account.
     * @param accountId - the id of the account whose messages are to be retrieved.
     * @return  a list of all messages posted by the account.
     */
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findAllPostedBy(accountId);
    }
}
