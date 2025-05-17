import java.time.LocalDateTime;
import java.util.Scanner;

import controller.MicrocontrollerController;
import controller.UserController;
import datastructures.AVL;
import datastructures.Node;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import utils.Color;
import utils.Location;

import utils.Color;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserController userController = new UserController();

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
            
            try {
                ans = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                ans = -1;
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