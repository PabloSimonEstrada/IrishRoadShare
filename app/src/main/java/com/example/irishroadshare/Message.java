// Package declaration
package com.example.irishroadshare;

// Import the required library for date manipulation
import java.util.Date;

// Declare a Message class
public class Message {

    // Declare variables for senderId, senderName, content, and timestamp
    private String senderId;
    private String senderName;
    private String content;
    private Date timestamp;

    // Default constructor for the Message class
    public Message() {
    }

    // Overloaded constructor for the Message class that takes senderId, senderName, content, and timestamp as parameters
    public Message(String senderId, String senderName, String content, Date timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getter method for senderId
    public String getSenderId() {
        return senderId;
    }

    // Setter method for senderId
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    // Getter method for senderName
    public String getSenderName() {
        return senderName;
    }

    // Setter method for senderName
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    // Getter method for content
    public String getContent() {
        return content;
    }

    // Setter method for content
    public void setContent(String content) {
        this.content = content;
    }

    // Getter method for timestamp
    public Date getTimestamp() {
        return timestamp;
    }

    // Setter method for timestamp
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}