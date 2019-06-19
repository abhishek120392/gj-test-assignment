package com.parkinglot.solution;

import com.parkinglot.solution.services.SlotAssignmentService;
import com.parkinglot.solution.services.SlotAssignmentServiceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws Exception {

        SlotAssignmentService slotAssignmentService = new SlotAssignmentServiceImpl();
        try {

            if (args[0] != null && args[0].length() > 0) {
                String fileName = args[0];
                executeFromFile(slotAssignmentService, fileName);
            } else {
                executeFromCommandLine(slotAssignmentService);
            }
        } catch (Exception e) {
            executeFromCommandLine(slotAssignmentService);
        }


    }

    /**
     * Execute Commands from Command Line / Terminal
     *
     * @param slotAssignmentService
     * @throws Exception
     */
    private static void executeFromCommandLine(SlotAssignmentService slotAssignmentService) throws Exception {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            if (!command.equalsIgnoreCase("exit")) {
               slotAssignmentService.executeCommand(command);
            } else
                break;
        }
    }

    /**
     * Execute Commands from Input File from given filename
     *
     * @param slotAssignmentService
     * @param fileName
     * @throws Exception
     */

    private static void executeFromFile(SlotAssignmentService slotAssignmentService, String fileName) throws Exception {
        Scanner scanner;
        List<String> commandlist = new ArrayList<>();
        File file = new File(fileName);
        scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            commandlist.add(scanner.nextLine());
        }
        slotAssignmentService.executeBatchCommands(commandlist);
    }


}
