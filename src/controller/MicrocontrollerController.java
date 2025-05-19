package controller;

import datastructures.AVL;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import service.MicrocontrollerService;
import utils.Color;
import utils.Location;

public class MicrocontrollerController {
   private MicrocontrollerService microcontrollerService;

   public MicrocontrollerController(AVL<Microcontroller> avl, AVL<ClimateRecord> avlRecords) {
      this.microcontrollerService = new MicrocontrollerService(avl, avlRecords);
   }

   public void addMicrocontroller(String name, Location location, String ipAddress) {
      try {
         microcontrollerService.addMicrocontroller(name, location, ipAddress);
         System.out.println(Color.successMessage("Microcontroller added successfully!"));
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public ClimateRecord createRegister(int microcontrollerId, double temperature, double humidity, double pressure) {
      try {
         ClimateRecord record = microcontrollerService.createRegister(microcontrollerId, temperature, humidity, pressure);
         System.out.println(Color.successMessage("Register created successfully!"));
         return record;
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
      }
   }

   public ClimateRecord updateRegister(int id, int microcontrollerId, double temperature, double humidity, double pressure) {
      try {
         ClimateRecord newRecord = new ClimateRecord(microcontrollerId, temperature, humidity, pressure);
         //microcontrollerService.updateRegister(id, newRecord);
         System.out.println(Color.successMessage("Register updated successfully!"));
         return newRecord;
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
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
}
