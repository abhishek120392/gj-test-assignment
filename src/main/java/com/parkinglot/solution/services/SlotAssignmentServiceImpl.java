package com.parkinglot.solution.services;

import com.parkinglot.solution.Exceptions.InvalidParametersException;
import com.parkinglot.solution.Exceptions.OperationNotSupportedException;
import com.parkinglot.solution.Exceptions.SlotNotfoundException;
import com.parkinglot.solution.models.Car;
import com.parkinglot.solution.models.MessagesEnum;
import com.parkinglot.solution.models.ParkingOperationsEnum;

import java.util.*;
import java.util.stream.Collectors;


public class SlotAssignmentServiceImpl implements SlotAssignmentService {

    private Integer parkingSlots = -1;
    private List<Car> carList = new ArrayList<>();
    private Map<Integer, Car> parkingSlotOccupancy = new TreeMap<>();

    public void setParkingSlots(Integer parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    /**
     * @param parameters
     * @param operation
     * @return Output String to Display
     * @throws InvalidParametersException
     */

    private void performOperation(List<String> parameters, ParkingOperationsEnum operation) throws InvalidParametersException {
        switch (operation) {

            case CREATE_PARKING_LOT:
                try {

                    setParkingSlots(Integer.parseInt(parameters.get(1)));


                    System.out.println("Created a parking lot with " + this.parkingSlots + " slots");
                } catch (Exception e) {
                    System.out.println("Invalid Parking Lot Strength Number");
                }
                break;


            case PARK:
                if (parameters.size() <= 1) {
                    System.out.println("Invalid Input Parameters");
                    break;
                }
                String registrationNumber = parameters.get(1);
                String colour = parameters.get(2);
                if (registrationNumber == null || colour == null || registrationNumber.isEmpty() || colour.isEmpty()) {
                    throw new InvalidParametersException("Car Details are insufficient : Please tru again");
                }
                Car car = new Car(registrationNumber, colour);
                assignParkingSlot(car);
                break;


            case STATUS:

                getCurrentStatus();
                break;


            case REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR:

                if (parameters.size() <= 1) {
                    System.out.println("Invalid Input Parameters");
                    break;
                }
                String color = parameters.get(1);
                String commaSeparatedRegNo = "";
                if (color != null && !color.isEmpty()) {
                    List<String> registrationNumbers = getRegistrationNumberByColor(color);


                    for (String registration : registrationNumbers) {
                        commaSeparatedRegNo += registration + ", ";
                    }
                    if (commaSeparatedRegNo.length() > 2) {
                        commaSeparatedRegNo = commaSeparatedRegNo.substring(0, commaSeparatedRegNo.length() - 2);
                    } else {
                        System.out.println(MessagesEnum.SLOT_NOT_FOUND.getParameter());
                        break;
                    }
                }
                System.out.println(commaSeparatedRegNo);
                break;


            case SLOT_NUMBERS_FOR_CARS_WITH_COLOUR:
                if (parameters.size() <= 1) {
                    System.out.println("Invalid Input Parameters");
                    break;
                }
                String carColor = parameters.get(1);
                if (carColor != null && !carColor.isEmpty()) {
                    List<Integer> slotIds = getSlotNumberByCarColor(carColor);
                    String commaSeparatedSlotIds = "";

                    for (Integer slotId : slotIds) {
                        commaSeparatedSlotIds += slotId + ", ";
                    }
                    if (commaSeparatedSlotIds.length() > 2) {

                        commaSeparatedSlotIds = commaSeparatedSlotIds.substring(0, commaSeparatedSlotIds.length() - 2);
                    } else {

                        System.out.println(MessagesEnum.SLOT_NOT_FOUND.getParameter());
                        break;
                    }
                    System.out.println(commaSeparatedSlotIds);
                    break;
                } else {
                    System.out.println("Invalid or No Car Color Mentioned");
                    break;
                }


            case SLOT_NUMBER_FOR_REGISTRATION_NUMBER:

                if (parameters.size() <= 1) {
                    System.out.println("Invalid Input Parameters");
                    break;
                }
                String regNo = parameters.get(1);

                if (regNo != null && !regNo.isEmpty()) {
                    Integer slot = getSlotNumberByVehicleRegistrationNumber(regNo);
                    if (slot != -1) {
                        System.out.println(String.valueOf(slot));
                        break;
                    }
                }
                System.out.println(MessagesEnum.SLOT_NOT_FOUND.getParameter());
                break;


            case LEAVE:
                if (parameters.size() <= 1) {
                    System.out.println("Invalid Input Parameters");
                    break;
                }
                Integer slotId = Integer.parseInt(parameters.get(1));

                releaseParkingSlot(slotId);
                break;


            default:
                System.out.println("Invalid Operation : operation not supported");
                break;

        }
    }


    /**
     * @param car
     * @return String to display whether slot assignment is successfull
     */

    public void assignParkingSlot(Car car) {

        Integer parkingSlot = getNextNearestParkingSlot();
        if (parkingSlot != -1) {

            car.setParkingSlotId(parkingSlot);
            carList.add(car);
            parkingSlotOccupancy.put(parkingSlot, car);


            System.out.println((MessagesEnum.SLOT_ALLOCATED.getParameter() + parkingSlot).replace("\n", ""));

        } else {
            System.out.println(MessagesEnum.SLOT_FULL.getParameter().replace("\n", ""));
        }

    }

    /**
     * Display current status of Parking slots and their occupancies
     *
     * @return String
     */

    public void getCurrentStatus() {

        System.out.println("Slot No.    Registration No    Colour");

        String message = "";
        Collections.sort(carList, new Comparator<Car>() {

            @Override
            public int compare(Car c1, Car c2) {
                return c1.getParkingSlotId() > c2.getParkingSlotId() ? 1 : -1;
            }

        });
        for (Car car : this.carList) {
            System.out.println(car.getParkingSlotId() + "           " +
                    car.getRegistrationNumber() + "      " +
                    car.getColor());
        }


    }

    /**
     * @param slotId
     * @return Message showing releasing of slot is successful or not
     */

    public void releaseParkingSlot(Integer slotId) {
        if (parkingSlotOccupancy.containsKey(slotId)) {
            Car vacatingCar = parkingSlotOccupancy.get(slotId);
            carList.remove(vacatingCar);
            parkingSlotOccupancy.remove(slotId);
            System.out.println("Slot number " + slotId + " is free");
        } else {
            System.out.println("Slot is already vacated or does not exist");
        }
    }

    /**
     * To get the next vacant parking slot nearest to the entry
     *
     * @return parking slot ID
     */
    private Integer getNextNearestParkingSlot() {


        if (carList.size() == this.parkingSlots)
            return -1;

        else {
            for (int i = 1; i <= this.parkingSlots; i++) {
                if (!parkingSlotOccupancy.containsKey(i)) {
                    return i;
                }
            }

            return -1;

        }
    }

    /**
     * below are some utility methods to get car, and parking slot details via filter
     *
     * @param colour
     * @return
     */

    public List<Car> getCarDetailsByColour(String colour) {

        return (carList.stream().filter(car -> car.getColor().equalsIgnoreCase(colour)).collect(Collectors.toList()));

    }

    public List<Car> getCarDetailsByRegistrationNumber(String registrationNumber) {

        return carList.stream().filter(car -> car.getRegistrationNumber().equalsIgnoreCase(registrationNumber)).collect(Collectors.toList());

    }


    public Integer getSlotNumberByVehicleRegistrationNumber(String registration) {

        List<Car> carsParked = getCarDetailsByRegistrationNumber(registration);

        if (carsParked != null && !carsParked.isEmpty()) {
            return carsParked.get(0).getParkingSlotId();
        }
        return -1;
    }

    public List<String> getRegistrationNumberByColor(String color) {

        List<Car> carsOfColor = getCarDetailsByColour(color);

        return (carsOfColor.stream().map(Car::getRegistrationNumber).collect(Collectors.toList()));

    }

    public List<Integer> getSlotNumberByCarColor(String color) {
        List<Car> carswithSameColor = getCarDetailsByColour(color);

        return (carswithSameColor.stream().map(Car::getParkingSlotId).collect(Collectors.toList()));
    }


    public void executeCommand(String command) throws SlotNotfoundException, OperationNotSupportedException, InvalidParametersException {

        List<String> parameters = new ArrayList<>(Arrays.asList(command.split(" ")));

        String primaryCommand = parameters.get(0);

        ParkingOperationsEnum operation = null;
        try {
            operation = ParkingOperationsEnum.valueOf(primaryCommand.toUpperCase());
        } catch (Exception e) {
            System.out.println("Parking operation \'" + parameters.get(0) + "\' is NOT SUPPORTED");
        }

        if (operation != null)
            performOperation(parameters, operation);
        else
            System.out.println("Parking operation " + parameters.get(0) + " is NOT SUPPORTED");

    }

    public void executeBatchCommands(List<String> commandList) {

        for (String command : commandList) {
            try {
                executeCommand(command);
            } catch (Exception e) {

            }

        }


    }

}
