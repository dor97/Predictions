package UI.ConsoleUI;

import DTO.*;
import gameEngien.gameEngine;
import gameEngien.world.World;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private static gameEngine m_gameEngine;
    public static void main(String args[]) {
        boolean validAction = true;
        m_gameEngine = new gameEngine();
        showMenu();
        int userChoice = readInput();

        while (userChoice != 5){

            executeUserChoice (userChoice);
            showMenu();
            userChoice = readInput();
        }
    }
    private static void executeUserChoice(int userChoice) {

        if (userChoice == 1){
            uploadXMLFile();
        }

        else if (userChoice == 2){
            displaySimulationDetails();
        }

        else if (userChoice == 3){
            runSimulation();
        }

        else if (userChoice == 4){
            displayPastSimulationDetails();
        }
    }
    private static void displayPastSimulationDetails() {

//        for (World simulation : m_gameEngine.getSimulationsList()){
//            System.out.println(simulation.getSimulationId());
//        }
    }
    private static void runSimulation() {
        List <DTOEnvironmentVariables> environmentVariables = m_gameEngine.getEnvironmentDetails();
        printEnvironmentVariablesAndSetValue(environmentVariables);

    }

    private static void printEnvironmentVariablesAndSetValue(List<DTOEnvironmentVariables> environmentVariables) {

        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Environment Variables:");
        boolean validAction;


        for (DTOEnvironmentVariables env_variable : environmentVariables) {
            validAction = false;
            System.out.println("Environment Variable Name : " + env_variable.getVariableName());
            System.out.println("Environment Variable Type : " + env_variable.getVariableType());
            if (env_variable.haveRange()){
                System.out.println("Environment Variable Range is: " +env_variable.getLowRange() + "-" + env_variable.getHighRange());
            }
            System.out.println("Please Set Value : ");
            env_variable.setValue(scanner.nextLine());
            while (!validAction){
                try{
                    m_gameEngine.addEnvironmentDto(env_variable);
                    System.out.println("Value Set Successfully");
                    validAction = true;
                }catch (InvalidValue e){
                    System.out.println(e.getMessage());
                    System.out.println("Please Set Value : ");
                    env_variable.setValue(scanner.nextLine());
                }
            }

        }
        System.out.println();
    }

    private static void displaySimulationDetails() {

        DTOSimulationDetails dtoSimulationDetails = m_gameEngine.getSimulationDetails();
        List <DTOEntityData> dtoEntityData = dtoSimulationDetails.getEntitysList();
        List < DTORuleData> dtoRuleData = dtoSimulationDetails.getRulesList();
        List < DTOTerminationData> dtoTerminationData = dtoSimulationDetails.getTerminationList();

        printEntitiesDetails(dtoEntityData);
        printRulesDetails(dtoRuleData);
        printTerminationDetails(dtoTerminationData);
    }
    private static void printTerminationDetails(List<DTOTerminationData> dtoTerminationData) {
        System.out.println();
        System.out.println("Termination conditions:");

        for (DTOTerminationData t_condition : dtoTerminationData) {
            System.out.println("Number Of " + t_condition.getType() + ": " + t_condition.getCount());
        }
        System.out.println();
    }
    private static void printRulesDetails(List<DTORuleData> dtoRuleData) {

        System.out.println();
        System.out.println("Rules:");

        for (DTORuleData rule : dtoRuleData) {
            System.out.println("Rule Name: " + rule.getRuleName());
            System.out.println("Number of Ticks : " + rule.getTicksToWorkAt());
            System.out.println("Probability : " + rule.getProbabilityToWork());
            System.out.println("Number of Actions : " + rule.getNumOfAction());

            System.out.println("Actions :");

            for (String action : rule.getActionsName()){
                System.out.println("Action Name: " + action);
            }
        }
    }
    private static void printEntitiesDetails(List<DTOEntityData> dtoEntityData) {

        System.out.println();
        System.out.println("Entities:");
        for (DTOEntityData entity : dtoEntityData) {
            System.out.println("Entity Name: " + entity.getName());
            System.out.println("Amount Of Entities: " + entity.getAmount());
            System.out.println("Properties:");
            for (DTOPropertyData property : entity.getPropertList()){
                System.out.println("Property Name: " + property.getName());
                System.out.println("Property Type: " +property.getType());
                if (property.haveRange()){
                    System.out.println("Property Range is: " +property.getLowRange() + "-" + property.getHighRange());
                }
                if (property.isRandomlyInatiated()){
                    System.out.println("Property has random initiate.");
                }
                else{
                    System.out.println("Property doesn't have random initiate.");
                }
            }
        }
    }
    private static void uploadXMLFile() {

        boolean validFile = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter full path of your XML file:");

        while (!validFile) {
            try {
                String fileName = scanner.nextLine();
                m_gameEngine.loadSimulation(fileName);
                validFile = true;
                System.out.println("File Upload Successfully");
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please Enter full path of your XML file:");
            }
        }
    }
    private static int readInput() {

        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        boolean inRange = false;
        int choice =0;

        while (!validInput && !inRange){
            try {
                choice = scanner.nextInt();

                if (choice < 1 || choice > 5) {
                    throw new IllegalArgumentException("Invalid input. you must choose number between 1 and 5.");
                }

                validInput = true;

            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a valid input.");
                showMenu();
            }catch (IllegalArgumentException e) {
                scanner.nextLine();
                System.out.println(e.getMessage());
                showMenu();
            }
        }
        return choice;
    }
    private static void showMenu() {
        System.out.println("Please choose one option by enter the number of option you want:");
        System.out.println("1. Upload XML file.");
        System.out.println("2. Display simulation details.");
        System.out.println("3. Run simulation.");
        System.out.println("4. Display past simulation details.");
        System.out.println("5. Exit");
    }
}
