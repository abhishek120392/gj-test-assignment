package com.parkinglot.solution.models;

public abstract class Vehicle {

    String registrationNumber;
    String color;
    Integer parkingSpace;
    Integer parkingSlotId = -1;

    public Vehicle(String resistractionNumber, String color) {
        this.registrationNumber = resistractionNumber;
        this.color = color;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getColor() {
        return color;
    }

    public Integer getParkingSpace() {
        return parkingSpace;
    }

    public Integer getParkingSlotId() {
        return parkingSlotId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setParkingSpace(Integer parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public void setParkingSlotId(Integer parkingSlotId) {
        this.parkingSlotId = parkingSlotId;
    }
}


