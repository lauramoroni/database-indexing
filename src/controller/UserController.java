package controller;

import model.entities.User;
import service.UserService;
import utils.Color;

public class UserController {
   UserService userService;

   public UserController() {
      this.userService = new UserService();
   }

   public void addUser(String id, String name, String password) {
      try {
         userService.addUser(id, name, password);
         System.out.println(Color.successMessage("User added successfully!"));
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public User login(String id, String password) throws Exception {
      return userService.login(id, password);
   }

   public void printUser(String id) {
      try {
         userService.printUser(id);
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public boolean exists(String id) {
      return userService.exists(id);
   }
}