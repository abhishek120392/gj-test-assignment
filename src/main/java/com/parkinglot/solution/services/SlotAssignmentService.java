package com.parkinglot.solution.services;

import com.parkinglot.solution.Exceptions.InvalidParametersException;
import com.parkinglot.solution.Exceptions.OperationNotSupportedException;
import com.parkinglot.solution.Exceptions.SlotNotfoundException;

import java.util.List;

public interface SlotAssignmentService {

    void executeCommand(String command) throws SlotNotfoundException, OperationNotSupportedException, InvalidParametersException;

    void executeBatchCommands(List<String> commandList);


}
