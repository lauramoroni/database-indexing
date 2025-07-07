package controller;

import datastructures.HashTable;
import datastructures.LinkedList;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import service.UserService;
import utils.Color;

public class UserController {
   UserService userService;

   public UserController(HashTable<Microcontroller> hashTableMicrocontrollers, HashTable<ClimateRecord> hashTableRecords, LinkedList<ClimateRecord> linkedlist) {
      this.userService = new UserService(hashTableMicrocontrollers, hashTableRecords, linkedlist);
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

   public void getAllRecords() {
      try {
         userService.getAllRecords();
      } catch (Exception e) {
         System.out.println(Color.errorMessage("Error retrieving records: " + e.getMessage()));
      }
   }

   public ClimateRecord getRecordById(int id) {
      try {
         return userService.getRecordById(id);
      } catch (Exception e) {
         System.out.println(Color.errorMessage( e.getMessage()));
         return null;
      }
   }

   public void removeRecord(int id, boolean isLog) {
      try {
         userService.removeRecord(id, isLog);
         System.out.println(Color.successMessage("Record removed successfully!"));
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

}