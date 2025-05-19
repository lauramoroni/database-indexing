import java.util.Scanner;

import controller.MicrocontrollerController;
import controller.UserController;
import datastructures.AVL;
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
        // Create 10 microcontrollers
        for (int i = 0; i < 3; i++) {
            String name = "MC " + (i + 1);
            Location location = Location.values()[i % Location.values().length];
            String ipAddress = "192.168.0." + (i + 1);
            microcontrollerController.addMicrocontroller(name, location, ipAddress);
        }

        // Create 10 climate records for each microcontroller
        for (int i = 0; i < 3; i++) {
            int microcontrollerId = i + 1;
            for (int j = 0; j < 3; j++) {
                double temperature = 20 + Math.random() * 10;
                double humidity = 50 + Math.random() * 20;
                double pressure = 1000 + Math.random() * 50;
                microcontrollerController.createRegister(microcontrollerId, temperature, humidity, pressure);
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
                    userController.getAllRecords();
                    break;
                case 2:
                    System.out.print(Color.inputPrompt("Enter the record ID: "));
                    int recordId = sc.nextInt();
                    ClimateRecord record = userController.getRecordById(recordId);
                    if (record != null) {
                        System.out.println(record.toString());
                    }
                    break;
                case 3:
                    System.out.print(Color.inputPrompt("Enter the Microcontroller ID: "));
                    int microcontrollerId = sc.nextInt();
                    ClimateRecord[] records = microcontrollerController.getRecordsByMicrocontrollerId(microcontrollerId);
                    if (records != null) {
                        for (ClimateRecord rec : records) {
                            System.out.println(rec.toString());
                        }
                    }
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
}