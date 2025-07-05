import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import controller.MicrocontrollerController;
import controller.UserController;
import datastructures.HashTable;
import datastructures.LinkedList;
import model.DAO.LogDAO;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import utils.Color;
import utils.Location;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static HashTable<Microcontroller> hashTableMicrocontrollers = new HashTable<>(100);
    private static HashTable<ClimateRecord> hashTableRecords = new HashTable<>(10);
    private static LinkedList<ClimateRecord> linkedList = new LinkedList<>();
    private static UserController userController = new UserController(hashTableMicrocontrollers, hashTableRecords, linkedList);
    private static MicrocontrollerController microcontrollerController = new MicrocontrollerController(
            hashTableMicrocontrollers, hashTableRecords, linkedList);

    public static void main(String[] args) throws Exception {
        clearFile();

        // Create 100 microcontrollers
        for (int i = 1; i <= 10; i++) {
            microcontrollerController.addMicrocontroller("Microcontroller " + i,
                    Location.values()[(int) (Math.random() * 4)],
                    "192.168.0." + (100 + i));
        }

        // Create 100 records for each microcontroller
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 1; j++) {
                microcontrollerController.createRecord(i, Math.random() * 100, Math.random() * 100,
                        Math.random() * 100);
            }
        }

        //clearHashTableFile();

        // User interaction
        clearScreen();
        System.out.println(Color.infoMessage("\r\n" + //
                        "                    _     _            _     __  __             _ _             _                _____           _                 \r\n" + //
                        "    /\\             | |   (_)          | |   |  \\/  |           (_) |           (_)              / ____|         | |                \r\n" + //
                        "   /  \\   _ __ ___ | |__  _  ___ _ __ | |_  | \\  / | ___  _ __  _| |_ ___  _ __ _ _ __   __ _  | (___  _   _ ___| |_ ___ _ __ ___  \r\n" + //
                        "  / /\\ \\ | '_ ` _ \\| '_ \\| |/ _ \\ '_ \\| __| | |\\/| |/ _ \\| '_ \\| | __/ _ \\| '__| | '_ \\ / _` |  \\___ \\| | | / __| __/ _ \\ '_ ` _ \\ \r\n" + //
                        " / ____ \\| | | | | | |_) | |  __/ | | | |_  | |  | | (_) | | | | | || (_) | |  | | | | | (_| |  ____) | |_| \\__ \\ ||  __/ | | | | |\r\n" + //
                        "/_/    \\_\\_| |_| |_|_.__/|_|\\___|_| |_|\\__| |_|  |_|\\___/|_| |_|_|\\__\\___/|_|  |_|_| |_|\\__, | |_____/ \\__, |___/\\__\\___|_| |_| |_|\r\n" + //
                        "                                                                                         __/ |          __/ |                      \r\n" + //
                        "                                                                                        |___/          |___/                       \r\n" + //
                        ""));
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
                "Quantity of records",
                "Simulate",
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
                    System.out.println(Color.header("Total Microcontrollers: " + microcontrollerController.getMicrocontrollerCount()));
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
                    int removeRecordId = sc.nextInt();
                    userController.removeRecord(removeRecordId);
                    break;
                case 7:
                    // Show record count
                    System.out.println(Color.header("Records Count"));
                    System.out
                            .println(Color.infoMessage("Total records: " + microcontrollerController.getRecordCount()));
                    LogDAO.saveLog("User " + userName + " viewed the record count.", "USER", "INFO");
                    break;
                case 8:
                    // Simulate new records
                    clearScreen();

                    System.out.println(
                            Color.menuOption("[1] Create new records\n[2] Update records\n[3] Remove records"));
                    System.out.print(Color.inputPrompt("Choose an option: "));
                    int simulationOption = sc.nextInt();
                    if (simulationOption < 1 || simulationOption > 3) {
                        System.out.println(Color.errorMessage("Invalid option!"));
                        break;
                    }

                    System.out.println(Color.header("Simulation ON"));
                    if (simulationOption == 1) {
                        System.out.println(Color.infoMessage("Creating 5 new records..."));
                        for (int i = 1; i <= 5; i++) {
                            microcontrollerController.createRecord(i, Math.random() * 100, Math.random() * 100,
                                    Math.random() * 100);
                        }
                        break;
                    } else if (simulationOption == 2) {
                        System.out.println(Color.infoMessage("Updating records..."));
                        System.out.println(Color.infoMessage("Updating 5 records..."));
                        for (int i = microcontrollerController.getRecordCount() - 4; i <= microcontrollerController.getRecordCount(); i++) {
                            microcontrollerController.updateRecord(i, Math.random() * 100,
                                    Math.random() * 100, Math.random() * 100);
                        }
                        break;
                    } else if (simulationOption == 3) {
                        System.out.println(Color.infoMessage("Removing records..."));
                        System.out.println(Color.infoMessage("Removing 5 records..."));
                        for (int i = 1; i <= 5; i++) {
                            userController.removeRecord(i);
                        }
                        break;
                    }

                    break;
                case 9:
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

    public static void clearAVLfile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/avl.txt"))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clearHashTableFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/database/hash_table.txt"))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile(String fileLog, String fileMicrocontrollers, String fileRecords, String fileAVL, String fileHashTable) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLog))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileHashTable))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFile() {
        clearFile("src/database/log.txt", "src/database/microcontrollers.txt", "src/database/records.txt",
                "src/database/avl.txt", "src/database/hash_table.txt");
    }
}