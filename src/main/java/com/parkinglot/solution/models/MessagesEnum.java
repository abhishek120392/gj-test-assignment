package com.parkinglot.solution.models;


public enum MessagesEnum {


    SLOT_FULL("Sorry, parking lot is full"),
    SLOT_ALLOCATED("Allocated slot number: "),
    SLOT_NOT_FOUND("Not found");

    private String parameter;

    MessagesEnum(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
