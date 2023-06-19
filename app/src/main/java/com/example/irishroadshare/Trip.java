// Package declaration
package com.example.irishroadshare;

public class Trip {
    // All of the fields of the Trip class
    private String additionalComments;  // Additional comments about the trip
    private String carMake;  // The make of the car used for the trip
    private String carModel;  // The model of the car used for the trip
    private String date;  // The date of the trip
    private String departure;  // The departure location of the trip
    private String destination;  // The destination of the trip
    private String licensePlate;  // The license plate of the car used for the trip
    private String luggageSize;  // The size of the luggage that can be accommodated on the trip
    private Boolean petsAllowed;  // Indicates whether pets are allowed on the trip
    private String price;  // The price of the trip
    private String seatsAvailable;  // The number of available seats on the trip
    private Boolean smokingAllowed;  // Indicates whether smoking is allowed on the trip
    private String time;  // The time of the trip
    private String userId;  // The ID of the user who posted the trip
    private String username;  // The username of the user who posted the trip

    // Getters and setters for each of the fields

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getLuggageSize() {
        return luggageSize;
    }

    public void setLuggageSize(String luggageSize) {
        this.luggageSize = luggageSize;
    }

    public Boolean getPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(Boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(String seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Boolean getSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(Boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
