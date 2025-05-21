package controller;

import datastructures.AVL;
import datastructures.LinkedList;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import service.MicrocontrollerService;
import utils.Color;
import utils.Location;

public class MicrocontrollerController {
   private MicrocontrollerService microcontrollerService;

   public MicrocontrollerController(AVL<Microcontroller> avl, AVL<ClimateRecord> avlRecords, LinkedList<ClimateRecord> linkedList) {
      this.microcontrollerService = new MicrocontrollerService(avl, avlRecords, linkedList);
   }

   public void addMicrocontroller(String name, Location location, String ipAddress) {
      try {
         microcontrollerService.addMicrocontroller(name, location, ipAddress);
         System.out.println(Color.successMessage("Microcontroller added successfully!"));
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public ClimateRecord createRecord(int microcontrollerId, double temperature, double humidity, double pressure) {
      try {
         ClimateRecord record = microcontrollerService.createRecord(microcontrollerId, temperature, humidity, pressure);
         System.out.println(Color.successMessage("Record created successfully!"));
         return record;
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
      }
   }

   public void updateRecord(int id, double temperature, double humidity, double pressure) {
      try {
         microcontrollerService.updateRecord(id, temperature, humidity, pressure);
         System.out.println(Color.successMessage("Record updated successfully!"));
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public ClimateRecord[] getRecordsByMicrocontrollerId(int microcontrollerId) {
      try {
         return microcontrollerService.getRecordsByMicrocontrollerId(microcontrollerId);
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
      return microcontrollerService.getRecordCount();
   }
}
