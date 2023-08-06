package UI.ConsoleUI;

import gameEngien.gameEngine;

import java.nio.file.NoSuchFileException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI {
    private static gameEngine m_gameEngine;
    public static void main(String args[]) {
        boolean validAction = true;
        m_gameEngine = new gameEngine();
        showMenu();
        int userChoice = readInput();

        while (userChoice != 5){

            validAction = executeUserChoice (userChoice);

            if (validAction == false){
                showMenu();
                userChoice = readInput();
            }

        }
    }
    private static boolean executeUserChoice(int userChoice) {

        boolean validAction = false;

        if (userChoice == 1){
            validAction = uploadXMLFile();
        }

        else if (userChoice == 2){
            validAction = displaySimulationDetails();
        }

        else if (userChoice == 3){
            validAction = runSimulation();
        }

        else if (userChoice == 4){
            validAction = displayPastSimulationDetails();
        }

        return validAction;
    }
    private static boolean displayPastSimulationDetails() {
        return false;
    }
    private static boolean runSimulation() {
        return false;
    }
    private static boolean displaySimulationDetails() {
        return false;
    }
    private static boolean uploadXMLFile() {

        boolean validFile = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter full path of your XML file:");

        while (!validFile) {
            try {
                String fileName = scanner.nextLine();
                m_gameEngine.lodSimuletion(fileName);
                validFile = true;
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
                return validFile;
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println(e.getMessage());
                System.out.println("Please Enter full path of your XML file:");
            }
        }

        return validFile;
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
