package UI.ConsoleUI;

import DTO.*;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import Engine.gameEngine;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.util.*;

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
            try{
                displayPastSimulationDetails();
            }catch (NoSuchElementException e){
             System.out.println(e.getMessage());
            }
        }
    }
    private static void displayPastSimulationDetails() {

        int maxID = getMaxID();
        if (maxID == 0){
            throw new NoSuchElementException("You must upload XML file to the System before you ask for past simulation details.");
        }
        int userInput_id = 0;
        int userInput_display = 0;
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);
        displaySimulationsOptions();

        while (!validInput){
            try {
                userInput_id = scanner.nextInt();
                if (userInput_id < 1 || userInput_id > maxID) {
                    throw new IllegalArgumentException("Invalid input. you must choose number from the options.");
                }
                validInput = true;
            }catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a valid input.");
                displaySimulationsOptions();
            }catch (IllegalArgumentException e){
                scanner.nextLine();
                System.out.println(e.getMessage());
                displaySimulationsOptions();
            }
        }
        validInput = false;

        while (!validInput){
            System.out.println("Please Choose Display Mode:");
            System.out.println("1. Entities quantity.");
            System.out.println("2. Properties Histogram.");
            try {
                userInput_display = scanner.nextInt();
                if (userInput_display < 1 || userInput_display > 2){
                    throw new IllegalArgumentException("Invalid input. you must choose the number from the options.");
                }
                validInput =true;
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid input.");
                scanner.nextLine();
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                scanner.nextLine();
            }
        }

        if (userInput_display == 1){
            printEntitiesQuantity(userInput_id);
        }
        else if (userInput_display == 2){
            printPropertiesHistogram(userInput_id);
        }

    }

    private static void printPropertiesHistogram(int id) {

        DTOSimulationDetailsPostRun postRun = m_gameEngine.getPostRunData(id);
        System.out.println("Please Select Entity :");
        int userInput = 0;
        int i =1;
        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);
        diaplayEntitiesOptions(postRun);

        while (!validInput){
            try {
                userInput = scanner.nextInt();

                if (userInput < 1 || userInput > postRun.getEntitiesPostRun().size()) {
                    throw new IllegalArgumentException("Invalid input. you must choose number from the options.");
                }
                validInput = true;

            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a valid input.");
                diaplayEntitiesOptions(postRun);
            }catch (IllegalArgumentException e) {
                scanner.nextLine();
                System.out.println(e.getMessage());
                diaplayEntitiesOptions(postRun);
            }
        }

        String Entity = findEntityByUserID(userInput, postRun);
        int propertiesNumber = printPropertiesOfEntity(Entity, postRun);
        int propertyOption = getPropertyInput(postRun, propertiesNumber, Entity);
        String property = findPropertyByOption(propertyOption, postRun, Entity);
        printHistogram(postRun.getHistogram(Entity, property));


    }

    private static void printHistogram(Map<Object, Integer> histogram) {

        for (Map.Entry<Object, Integer> value : histogram.entrySet()){
            System.out.println( value.getKey() + ":" + value.getValue());
        }
    }

    private static String findPropertyByOption(int propertyOption, DTOSimulationDetailsPostRun postRun, String entity) {
        int i = 1;
        String propertyName = "";

        for (Map.Entry<String, DTOEntitysProperties> property : postRun.getEntitysProperties().entrySet()){
            if (Objects.equals(entity, property.getKey())){
                for (DTOProperty entityProp : property.getValue().getProperties()){
                    if (i == propertyOption){
                        propertyName = entityProp.getName();
                        break;
                    }
                    i++;
                }
            }
        }
        return propertyName;

    }

    private static int getPropertyInput(DTOSimulationDetailsPostRun postRun, int propertiesNumber, String entity) {

        boolean validInput = false;
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        while (!validInput) {
            try {
                userInput = scanner.nextInt();

                if (userInput < 1 || userInput > propertiesNumber) {
                    throw new IllegalArgumentException("Invalid input. you must choose number from the options.");
                }
                validInput = true;

            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a valid input.");
                printPropertiesOfEntity(entity, postRun);
            } catch (IllegalArgumentException e) {
                scanner.nextLine();
                System.out.println(e.getMessage());
                printPropertiesOfEntity(entity, postRun);
            }
        }
        return userInput;
    }

    private static int printPropertiesOfEntity(String entity, DTOSimulationDetailsPostRun postRun) {

        int i = 1;
        System.out.println("Please Choose Property:");
        for (Map.Entry<String, DTOEntitysProperties> property : postRun.getEntitysProperties().entrySet()){
            if (Objects.equals(entity, property.getKey())){
                for (DTOProperty entityProp : property.getValue().getProperties()){
                    System.out.println(i+ "." + entityProp.getName());
                    i++;
                }
            }
        }
        return i - 1;
    }

    private static String findEntityByUserID(int userInput, DTOSimulationDetailsPostRun postRun) {

        int i= 1;
        String entityName= "";
        for(DTOEntityPostRun entity : postRun.getEntitiesPostRun()){
            if (userInput == i){
                entityName = entity.getName();
                break;
            }
        }
        return entityName;
    }

    private static void diaplayEntitiesOptions(DTOSimulationDetailsPostRun postRun) {

        int i= 1;
        for(DTOEntityPostRun entity : postRun.getEntitiesPostRun()){
            System.out.println(i+ "." + entity.getName());
            i++;
        }
    }

    private static void printEntitiesQuantity(int id) {

        DTOSimulationDetailsPostRun postRun = m_gameEngine.getPostRunData(id);
        for(DTOEntityPostRun entity : postRun.getEntitiesPostRun()){
            System.out.println("Entity Name : " + entity.getName());
            System.out.println("Amount Pre Run : " + entity.getAmountPreRun());
            System.out.println("Amount Post Run : " + entity.getAmountPostRun());
            System.out.println();
        }
    }

    private static int getMaxID() {
        int maxID = 0;
        for (Map.Entry<Integer,String> simulation : m_gameEngine.getSimulationDto().getSimulations().entrySet()){
            if (simulation.getKey() > maxID){
                maxID = simulation.getKey();
            }
        }

        return maxID;
    }
    private static void displaySimulationsOptions(){
        System.out.println("Please Choose Simulation By ID: ");
        for (Map.Entry<Integer,String> simulation : m_gameEngine.getSimulationDto().getSimulations().entrySet()){
            System.out.println("ID : " + simulation.getKey());
            System.out.println("Run Date : " + simulation.getValue());
            System.out.println();
        }
    }
    private static void runSimulation() {
        Scanner scanner = new Scanner(System.in);
        int simuladtion_id;
        List <DTOEnvironmentVariables> environmentVariables = m_gameEngine.getEnvironmentDetails();

        printEnvironmentVariablesAndSetValue(environmentVariables);
        System.out.println("Press Enter to run the simulation : ");
        scanner.nextLine();
        try {
            List <DTOEnvironmentVariablesValues> environmentVariablesValues = m_gameEngine.setSimulation();
            printEnvironmentVariablesValues(environmentVariablesValues);
        }catch (InvalidValue e){
            System.out.println(e.getMessage());
        }
        try {
            simuladtion_id= m_gameEngine.activeSimulation();
            System.out.println("Simulation ID : " + simuladtion_id);
        }catch (InvalidValue | ReferenceNotInitializedException e){
            System.out.println(e.getMessage());
        }
    }
    private static void printEnvironmentVariablesValues(List<DTOEnvironmentVariablesValues> environmentVariablesValues) {

        System.out.println();

        for (DTOEnvironmentVariablesValues env_variable_value : environmentVariablesValues){
            System.out.println("Environment Variable Name : " + env_variable_value.getName());
            System.out.println("Environment Variable Value : " + env_variable_value.getValue());
            System.out.println();
        }
    }
    private static void printEnvironmentVariablesAndSetValue(List<DTOEnvironmentVariables> environmentVariables) {

        String userInput;
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
            System.out.println("Please Set Value (if you don't want to set press enter) : ");
            userInput = scanner.nextLine();
            if (!userInput.isEmpty()){
                env_variable.setValue(userInput);
                while (!validAction){
                    try{
                        m_gameEngine.addEnvironmentDto(env_variable);
                        System.out.println("Values Set Successfully");
                        validAction = true;
                    }catch (InvalidValue e){
                        System.out.println(e.getMessage());
                        System.out.println("Please Set Value (if you don't want to set press enter) : ");
                        userInput = scanner.nextLine();
                        if (!userInput.isEmpty()){
                            env_variable.setValue(userInput);
                        }
                        else {
                            validAction = true;
                        }
                    }
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
                System.out.println("File Uploaded Successfully");
                System.out.println();
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
        int choice =0;

        while (!validInput){
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
