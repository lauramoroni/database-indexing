package controller;

import datastructures.HashTable;
import datastructures.LinkedList;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import protocol.Message;
import protocol.MessageHandler;
import service.MicrocontrollerService;
import utils.Color;
import utils.Location;

public class MicrocontrollerController {
   private MicrocontrollerService microcontrollerService;
   private MessageHandler messageHandler;

   public MicrocontrollerController(HashTable<Microcontroller> hashTableMicrocontrollers, HashTable<ClimateRecord> hashTableRecords, LinkedList<ClimateRecord> linkedList) {
      this.microcontrollerService = new MicrocontrollerService(hashTableMicrocontrollers, hashTableRecords, linkedList);
      this.messageHandler = new MessageHandler(microcontrollerService);
   }

   public void addMicrocontroller(String name, Location location, String ipAddress, boolean isLog) {
      try {
         Microcontroller microcontroller = new Microcontroller(name, location, ipAddress);
         Message message = new Message("CREATE_MC", microcontroller);
         Message response = messageHandler.handleMessage(message, isLog);
         if (response.getContent().equals("Microcontroller created successfully")) {
            System.out.println(Color.successMessage("Microcontroller added successfully!"));
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public ClimateRecord createRecord(int microcontrollerId, double temperature, double humidity, double pressure, boolean isLog) {
      try {
         ClimateRecord record = new ClimateRecord(microcontrollerId, temperature, humidity, pressure);
         Message message = new Message("CREATE_CR", record);
         Message response = messageHandler.handleMessage(message, isLog);
         if (response.getContent().startsWith("Climate record created successfully")) {
            System.out.println(Color.successMessage("Record created successfully!"));
            return response.getClimateRecord();
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
            return null;
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
      }
   }

   public void updateRecord(int id, double temperature, double humidity, double pressure, boolean isLog) {
      try {
         Message message = new Message("UPDATE_CR", id);
         Message response = messageHandler.handleMessage(message, isLog);
         if (response.getContent().equals("Climate record updated successfully")) {
            System.out.println(Color.successMessage("Record updated successfully!"));
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public ClimateRecord[] getRecordsByMicrocontrollerId(int microcontrollerId) {
      try {
         Message message = new Message("GET_CR_BY_MC", microcontrollerId);
         Message response = messageHandler.handleMessage(message, false);
         if (response.getContent().startsWith("Records retrieved successfully")) {
            return response.getClimateRecords();
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
            return null;
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
      }
   }

   public void printMicrocontroller(int id) {
      try {
         microcontrollerService.printMicrocontroller(id);
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public void printAllMicrocontrollers() {
      try {
         microcontrollerService.printAllMicrocontrollers();
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public int getRecordCount() {
      try {
         Message message = new Message("GET_CR_COUNT");
         Message response = messageHandler.handleMessage(message, false);
         if (response.getContent().startsWith("Record count retrieved successfully")) {
            return response.getCount();
         } else {
            System.out.println(Color.errorMessage(response.getContent()));
            return 0;
         }
      } catch (Exception e) {
         System.out.println(Color.errorMessage("Error retrieving record count: " + e.getMessage()));
         return 0;
      }
   }

   public int getMicrocontrollerCount() {
      Message message = new Message("GET_MC_COUNT");
      Message response = messageHandler.handleMessage(message, false);
      if (response.getContent().startsWith("Microcontroller count retrieved successfully")) {
         return response.getCount();
      } else {
         System.out.println(Color.errorMessage(response.getContent()));
         return 0;
      }
   }
}
