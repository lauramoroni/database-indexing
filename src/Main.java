import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import controller.MicrocontrollerController;
import controller.UserController;
import datastructures.AVL;
import model.DAO.LogDAO;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import utils.Color;
import utils.Location;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static AVL<ClimateRecord> avlRecords = new AVL<>();
    private static AVL<Microcontroller> avlMicrocontroller = new AVL<>();
    private static UserController userController = new UserController(avlRecords);
    private static MicrocontrollerController microcontrollerController = new MicrocontrollerController(
            avlMicrocontroller, avlRecords);

    public static void main(String[] args) throws Exception {
        clearFile();

        // Create 10 microcontrollers
        for (int i = 1; i <= 10; i++) {
            microcontrollerController.addMicrocontroller("Microcontroller " + i, Location.MACEIO,
                    "192.168.0." + i);
        }

        // Create 10 records for each microcontroller
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                microcontrollerController.createRegister(i, 25.0 + j, 60.0 + j, 1013.0 + j);
            }
        }

        // User interaction
        clearScreen();
        System.out.println(Color.header("Welcome to the Ambiental Monitoring System!"));
        sc.nextLine();

        User loggedUser = null;

        while (loggedUser == null) {
            clearScreen();

            System.out.print(Color.inputPrompt("Please enter your CPF: "));
            String cpf = sc.nextLine();
            System.out.print(Color.inputPrompt("Please enter your password: "));
            String password = sc.nextLine();

            try {
                loggedUser = userController.login(cpf, password);
                System.out.println(Color.successMessage("Login successful. Welcome, " + loggedUser.getName() + "!"));
            } catch (Exception e) {
                System.out.println(Color.errorMessage(e.getMessage()));

                if (e.getMessage().equals("User not found.")) {
                    System.out.print(Color.warningMessage("Do you want to create a new user? (y/n): "));
                    String ans = sc.nextLine().toLowerCase();
                    if (ans.equals("y")) {
                        clearScreen();
                        System.out.print(Color.inputPrompt("Enter your name: "));
                        String name_newlogin = sc.nextLine();
                        System.out.print(Color.inputPrompt("Enter your CPF: "));
                        String cpf_newlogin = sc.nextLine();
                        System.out.print(Color.inputPrompt("Enter your password: "));
                        String password_newlogin = sc.nextLine();

                        try {
                            userController.addUser(cpf_newlogin, name_newlogin, password_newlogin);
                            System.out.println(Color.successMessage("User registered successfully!"));
                        } catch (Exception ex) {
                            System.out.println(Color.errorMessage(ex.getMessage()));
                        }
                    } else {
                        System.out.println(Color.warningMessage("Registration cancelled."));
                    }
                }
            }
        }

        showMenu(loggedUser.getName(), sc);
        sc.close();
    }

    public static void showMenu(String userName, Scanner sc) {
        String[] options = {
                "Show all records",
                "Show records by ID",
                "Show records by Microcontroller ID",
                "Show all microcontrollers",
                "Show microcontroller details",
                "Remove records",
                "Quantity of records"
        };

        int ans = -1;

        do {
            clearScreen();
            System.out.println(Color.header("Menu - Ambient Monitoring System - " + userName));

            for (int i = 0; i < options.length; i++) {
                System.out.println(Color.MENU_OPTION + "[" + (i + 1) + "] " + options[i] + Color.RESET);
            }
            System.out.println(Color.MENU_OPTION + "[" + (options.length + 1) + "] Exit" + Color.RESET);

            System.out.print(Color.inputPrompt("Choose an option: "));

            ans = sc.nextInt();

            switch (ans) {
                case 1:
                    System.out.println(Color.header("All Records:"));
                    userController.getAllRecords();
                    LogDAO.saveLog("User " + userName + " viewed all records.", "USER", "INFO");
                    break;
                case 2:
                    System.out.print(Color.inputPrompt("Enter the record ID: "));
                    int recordId = sc.nextInt();
                    ClimateRecord record = userController.getRecordById(recordId);
                    if (record != null) {
                        System.out.println(record.toString());
                    }
                    LogDAO.saveLog("User " + userName + " viewed record " + recordId, "USER", "INFO");
                    break;
                case 3:
                    System.out.print(Color.inputPrompt("Enter the Microcontroller ID: "));
                    int microcontrollerId = sc.nextInt();
                    ClimateRecord[] records = microcontrollerController
                            .getRecordsByMicrocontrollerId(microcontrollerId);
                    if (records != null) {
                        for (ClimateRecord rec : records) {
                            System.out.println(rec.toString());
                        }
                    }
                    LogDAO.saveLog("User " + userName + " viewed records for Microcontroller " + microcontrollerId,
                            "USER", "INFO");
                    break;
                case 4:
                    // Show all microcontrollers
                    System.out.println(Color.header("Microcontrollers:"));
                    microcontrollerController.printAllMicrocontrollers();
                    LogDAO.saveLog("User " + userName + " viewed all microcontrollers.", "USER", "INFO");
                    break;
                case 5:
                    // Show microcontroller details
                    System.out.print(Color.inputPrompt("Enter the Microcontroller ID: "));
                    int mcId = sc.nextInt();
                    microcontrollerController.printMicrocontroller(mcId);
                    LogDAO.saveLog("User " + userName + " viewed microcontroller " + mcId, "USER", "INFO");
                    break;
                case 6:
                    // Remove records
                    System.out.print(Color.inputPrompt("Enter the record ID to remove: "));
                    int recordIdToRemove = sc.nextInt();
                    userController.removeRecord(recordIdToRemove);
                    LogDAO.saveLog("Removed record " + recordIdToRemove, "CR", "REMOVE");
                    break;
                case 7:
                    // Show record count
                    System.out.println(Color.header("Microcontroller Record Count"));
                    System.out.println(Color.infoMessage("Total records: " + microcontrollerController.getRecordCount()));
                    LogDAO.saveLog("User " + userName + " viewed the record count.", "USER", "INFO");
                    break;
                case 8:
                    // Exit
                    LogDAO.saveLog("User " + userName + " exited the system.", "USER", "INFO");
                    clearScreen();
                    System.out.println(Color.successMessage("Exiting the system..."));
                    System.out.println(Color.highlight("Thank you for using the Ambient Monitoring System!"));
                    System.exit(0);
                    break;
                default:
                    break;
            }

            if (ans != options.length + 1) {
                System.out.println(Color.infoMessage("Press ENTER to continue..."));
                sc.nextLine();
            }
        } while (ans != options.length + 1);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void clearFile(String fileMicrocontrollers, String fileRecords, String fileAVL) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileMicrocontrollers))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileRecords))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileAVL))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile() {
        clearFile("src/database/microcontrollers.txt", "src/database/records.txt", "src/database/avl.txt");
    }
}