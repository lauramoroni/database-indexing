package controller;

import datastructures.HashTable;
import datastructures.LinkedList;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import protocol.Message;
import protocol.MessageHandler;
import service.UserService;
import utils.Color;

public class UserController {
   UserService userService;
   MessageHandler messageHandler;

   public UserController(HashTable<Microcontroller> hashTableMicrocontrollers, HashTable<ClimateRecord> hashTableRecords, LinkedList<ClimateRecord> linkedlist) {
      this.userService = new UserService(hashTableMicrocontrollers, hashTableRecords, linkedlist);
      this.messageHandler = new MessageHandler(userService);
   }

   public void addUser(String id, String name, String password) {
      try {
         Message message = new Message("CREATE_USER", id, name, password);
         Message response = messageHandler.handleMessage(message, true);
         if (response.getContent().equals("User created successfully")) {
            System.out.println(Color.successMessage("User added successfully!"));
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public User login(String id, String password) throws Exception {
      Message message = new Message("LOGIN", id, password);
      Message response = messageHandler.handleMessage(message, true);
      if (response.getContent().startsWith("User logged in successfully")) {
         return response.getUser();
      } else {
         throw new Exception(response.getContent());
      }
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
         Message message = new Message("GET_ALL");
         Message response = messageHandler.handleMessage(message, false);
         if (response.getContent().equals("All records retrieved successfully")) {
            System.out.println(Color.successMessage("All records retrieved successfully!"));
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage("Error retrieving records: " + e.getMessage()));
      }
   }

   public ClimateRecord getRecordById(int id) {
      try {
         Message message = new Message("GET_CR_BY_ID", id);
         Message response = messageHandler.handleMessage(message, false);
         if (response.getContent().startsWith("Record retrieved successfully")) {
            return response.getClimateRecord();
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
            return null;
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage( e.getMessage()));
         return null;
      }
   }

   public void removeRecord(int id, boolean isLog) {
      try {
         Message message = new Message("REMOVE_CR", id);
         Message response = messageHandler.handleMessage(message, isLog);
         if (response.getContent().equals("Record removed successfully")) {
            System.out.println(Color.successMessage("Record removed successfully!"));
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

}