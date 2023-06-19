// We define the package in which our class is located
package com.example.irishroadshare;

// We import the Date class which is needed for timestamp
import java.util.Date;

// The Conversation class represents an interaction between two users
public class Conversation {

    // Define instance variables with meaningful names
    private String id, tripId, requesterId, tripOwnerId, lastMessage, departure, destination, price, date, time, senderName;
    private Date timestamp; // Stores the timestamp of the conversation
    private int seatsToBook; // Stores the number of seats to be booked

    // Default constructor for the Conversation class
    public Conversation() {
    }

    // Parametrized constructor for the Conversation class
    public Conversation(String id, String tripId, String requesterId, String tripOwnerId, String lastMessage, Date timestamp, String departure, String destination, String price, String date, String time, String senderName, int seatsToBook) {
        this.id = id;
        this.tripId = tripId;
        this.requesterId = requesterId;
        this.tripOwnerId = tripOwnerId;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.departure = departure;
        this.destination = destination;
        this.price = price;
        this.date = date;
        this.time = time;
        this.senderName = senderName;
        this.seatsToBook = seatsToBook;
    }

    // Method to get the ID of the other user in the conversation
    public String getOtherUserId(String currentUserId) {
        if (currentUserId.equals(requesterId)) {
            return tripOwnerId;
        } else {
            return requesterId;
        }
    }

    // Getters and Setters for the instance variables

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getTripOwnerId() {
        return tripOwnerId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeatsToBook() {
        return seatsToBook;
    }

    public void setSeatsToBook(int seatsToBook) {
        this.seatsToBook = seatsToBook;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}