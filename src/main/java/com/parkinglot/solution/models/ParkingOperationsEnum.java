package com.parkinglot.solution.models;

public enum ParkingOperationsEnum {

    CREATE_PARKING_LOT("create_parking_lot"),
    PARK("park"),
    STATUS("status"),
    REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR("registration_numbers_for_cars_with_colour"),
    SLOT_NUMBERS_FOR_CARS_WITH_COLOUR("slot_numbers_for_cars_with_colour"),
    SLOT_NUMBER_FOR_REGISTRATION_NUMBER("slot_number_for_registration_number"),
    LEAVE("leave");

    private String action;

    ParkingOperationsEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }


}
