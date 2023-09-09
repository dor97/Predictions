package UI.ConsoleUI;

import DTO.*;
import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import Engine.Engine;
import Engine.myTask;
import javafx.beans.property.*;
import org.omg.CORBA.DynAnyPackage.InvalidValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ConsoleUI {
    private Engine m_Engine;
    private myTask m_task;

    private BooleanProperty pause = new SimpleBooleanProperty();
    private IntegerProperty tick = new SimpleIntegerProperty();

    public ConsoleUI(myTask task){
        m_task = task;


    }
    public void start() {

        pause.bindBidirectional(m_task.pauseProperty());
        pause.set(false);
        tick.bindBidirectional(m_task.tickProperty());
        tick.set(0);

        boolean validAction = true;
        m_Engine = new Engine();

        showMenu();
        int userChoice = readInput();

        while (userChoice != 7){

            executeUserChoice (userChoice);
            showMenu();
            userChoice = readInput();
        }
    }
    private void executeUserChoice(int userChoice) {

        if (userChoice == 1){
            uploadXMLFile();
        }

        else if (userChoice == 2){
            try{
                displaySimulationDetails();
            }catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }

        }

        else if (userChoice == 3){
            try{
                runSimulation();
            }catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }
        }

        else if (userChoice == 4){
            try{
                displayPastSimulationDetails();
            }catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }
        }
        else if (userChoice == 5){
            try{
                saveStateTofile();
            }catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }
        }
        else if (userChoice == 6){
            try {
                loadStateFromFile();
            }catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }
        } else if (userChoice == 8) {
            pause.set(true);
            System.out.println(tick.get());
//            int t = tick.get();
//            if(t % 1000 == 0){
//                System.out.println(t);
//            }
        } else if (userChoice == 9) {
            synchronized(m_task) {
                pause.set(false);
                m_task.notifyAll();
            }
        } else if (userChoice == 10) {

        }
    }

    private void loadStateFromFile() {

        Scanner scanner = new Scanner(System.in);
        String input = "";
        System.out.println("Please Enter full path of your file without extension:");
        input = scanner.nextLine();
        try{
            m_Engine.loadSystemState(input);
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch (IOException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private void saveStateTofile() {

        if (getMaxID() == 0){
            throw new NoSuchElementException("You must upload XML file to the System OR load the state from file before you ask to save the current state to file.");
        }
        Scanner scanner = new Scanner(System.in);
        String input = "";
        System.out.println("Please Enter full path of your file without extension: ");
        input = scanner.nextLine();
        try{
            m_Engine.saveSystemState(input);
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void displayPastSimulationDetails() {

        int maxID = getMaxID();
        if (maxID == 0){
            throw new NoSuchElementException("You must upload XML file to the System AND run OR load state from file before you ask to save system state.");
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
    private void printPropertiesHistogram(int id) {

        DTOSimulationDetailsPostRun postRun = m_Engine.getPostRunData(id);
        System.out.println("Please Select Entity:");
        System.out.println("--------------------------");
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
    private void printHistogram(Map<Object, Integer> histogram) {

        for (Map.Entry<Object, Integer> value : histogram.entrySet()){
            System.out.println( value.getKey() + ":" + value.getValue());
        }
        System.out.println();
    }
    private String findPropertyByOption(int propertyOption, DTOSimulationDetailsPostRun postRun, String entity) {
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
    private int getPropertyInput(DTOSimulationDetailsPostRun postRun, int propertiesNumber, String entity) {

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
    private int printPropertiesOfEntity(String entity, DTOSimulationDetailsPostRun postRun) {

        int i = 1;
        System.out.println("Please Choose Property:");
        System.out.println("----------------------------");
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
    private String findEntityByUserID(int userInput, DTOSimulationDetailsPostRun postRun) {

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
    private void diaplayEntitiesOptions(DTOSimulationDetailsPostRun postRun) {

        int i= 1;
        for(DTOEntityPostRun entity : postRun.getEntitiesPostRun()){
            System.out.println(i+ "." + entity.getName());
            i++;
        }
    }
    private void printEntitiesQuantity(int id) {

        DTOSimulationDetailsPostRun postRun = m_Engine.getPostRunData(id);
        for(DTOEntityPostRun entity : postRun.getEntitiesPostRun()){
            System.out.println("Entity Name : " + entity.getName());
            System.out.println("Amount Pre Run : " + entity.getAmountPreRun());
            System.out.println("Amount Post Run : " + entity.getAmountPostRun());
            System.out.println();
        }
    }
    private int getMaxID() {
        int maxID = 0;
        for (Map.Entry<Integer,String> simulation : m_Engine.getSimulationDto().getSimulations().entrySet()){
            if (simulation.getKey() > maxID){
                maxID = simulation.getKey();
            }
        }

        return maxID;
    }
    private void displaySimulationsOptions(){
        System.out.println("Please Choose Simulation By ID: ");
        System.out.println("------------------------------------------");
        for (Map.Entry<Integer,String> simulation : m_Engine.getSimulationDto().getSimulations().entrySet()){
            System.out.println("    ID : " + simulation.getKey());
            System.out.println("    Run Date : " + simulation.getValue());
            System.out.println();
        }
    }
    private void runSimulation() {

        if (m_Engine.getCurrentSimulation() == null){
            throw new NoSuchElementException("You must upload XML file before you ask to run simulation");
        }
        Scanner scanner = new Scanner(System.in);
        int simuladtion_id;
        List <DTOEnvironmentVariables> environmentVariables = m_Engine.getEnvironmentDetails();

        printEnvironmentVariablesAndSetValue(environmentVariables);
        System.out.println("Press Enter to run the simulation : ");
        scanner.nextLine();
        try {
            List <DTOEnvironmentVariablesValues> environmentVariablesValues = m_Engine.setSimulation();
            printEnvironmentVariablesValues(environmentVariablesValues);
        }catch (InvalidValue e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            simuladtion_id= m_Engine.activeSimulation(m_task);
            System.out.println("Simulation ID : " + simuladtion_id);
            System.out.println();
        }catch (InvalidValue | ReferenceNotInitializedException e){
            System.out.println(e.getMessage());
        }
    }
    private void printEnvironmentVariablesValues(List<DTOEnvironmentVariablesValues> environmentVariablesValues) {

        System.out.println();

        for (DTOEnvironmentVariablesValues env_variable_value : environmentVariablesValues){
            System.out.println("Environment Variable Name : " + env_variable_value.getName());
            System.out.println("Environment Variable Value : " + env_variable_value.getValue());
            System.out.println();
        }
    }
    private void printEnvironmentVariablesAndSetValue(List<DTOEnvironmentVariables> environmentVariables) {

        String userInput;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Environment Variables:");
        System.out.println("----------------------------");
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
                        m_Engine.addEnvironmentDto(env_variable);
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
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
    private void displaySimulationDetails() {

        if (m_Engine.getCurrentSimulation() == null){
            throw new NoSuchElementException("You must upload XML file before you ask to display simulation details");
        }
        DTOSimulationDetails dtoSimulationDetails = m_Engine.getSimulationDetails();
        List <DTOEntityData> dtoEntityData = dtoSimulationDetails.getEntitysList();
        List < DTORuleData> dtoRuleData = dtoSimulationDetails.getRulesList();
        //List < DTOTerminationData> dtoTerminationData = dtoSimulationDetails.getTerminationList();

        printEntitiesDetails(dtoEntityData);
        printRulesDetails(dtoRuleData);
        //printTerminationDetails(dtoTerminationData);
    }
    private void printTerminationDetails(List<DTOTerminationData> dtoTerminationData) {
        System.out.println();
        System.out.println("Termination conditions:");
        System.out.println("----------------------------");

        for (DTOTerminationData t_condition : dtoTerminationData) {
            //System.out.println("Number Of " + t_condition.getType() + ": " + t_condition.getCount());
        }
        System.out.println();
    }
    private void printRulesDetails(List<DTORuleData> dtoRuleData) {

        int i = 1;
        System.out.println();
        System.out.println("Rules:");
        System.out.println("--------------");

        for (DTORuleData rule : dtoRuleData) {
            System.out.println("Rule Name: " + rule.getRuleName());
            System.out.println("Number of Ticks : " + rule.getTicksToWorkAt());
            System.out.println("Probability : " + rule.getProbabilityToWork());
            System.out.println("Number of Actions : " + rule.getNumOfAction());
            System.out.println();

            System.out.println("    Actions:");
            System.out.println("    --------------");

            for (String action : rule.getActionsName()){
                System.out.println("    Action Name: " + action);
            }
            System.out.println();
        }
    }
    private void printEntitiesDetails(List<DTOEntityData> dtoEntityData) {

        System.out.println();
        System.out.println("Entities:");
        System.out.println("--------------");
        for (DTOEntityData entity : dtoEntityData) {
            System.out.println("Entity Name: " + entity.getName());
            System.out.println("Amount Of Entities: " + entity.getAmount());
            System.out.println();
            System.out.println("    Properties:");
            System.out.println("    --------------");
            for (DTOPropertyData property : entity.getPropertList()){
                System.out.println("    Property Name: " + property.getName());
                System.out.println("    Property Type: " +property.getType());
                if (property.haveRange()){
                    System.out.println("    Property Range is: " +property.getLowRange() + "-" + property.getHighRange());
                }
                if (property.isRandomlyInatiated()){
                    System.out.println("    Property has random initiate.");
                }
                else{
                    System.out.println("    Property doesn't have random initiate.");
                }
                System.out.println();
            }
        }
    }
    private void uploadXMLFile() {

        boolean validFile = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter full path of your XML file:");

        while (!validFile) {
            try {
                String fileName = scanner.nextLine();
                m_Engine.loadSimulation(fileName);
                validFile = true;
                System.out.println("File Uploaded Successfully!");
                System.out.println();
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please Enter full path of your XML file:");
            }
        }
    }
    private int readInput() {

        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;
        int choice =0;

        while (!validInput){
            try {
                choice = scanner.nextInt();

                if (choice < 1 || choice > 9) {
                    throw new IllegalArgumentException("Invalid input. you must choose number between 1 and 7.");
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
    private void showMenu() {
        System.out.println("Please choose one option by enter the number of option you want:");
        System.out.println("1. Upload XML file.");
        System.out.println("2. Display simulation details.");
        System.out.println("3. Run simulation.");
        System.out.println("4. Display past simulation details.");
        System.out.println("5. Save state to file.");
        System.out.println("6. Load state from file.");
        System.out.println("7. Exit");
    }
}
