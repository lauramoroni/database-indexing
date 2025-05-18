package controller;

import model.entities.ClimateRecord;
import service.MicrocontrollerService;
import utils.Color;
import utils.Location;

public class MicrocontrollerController {
   private MicrocontrollerService microcontrollerService;

   public MicrocontrollerController() {
      this.microcontrollerService = new MicrocontrollerService();
   }

   public void addMicrocontroller(String id, String name, Location location, String ipAddress) throws Exception {
      try {
         microcontrollerService.addMicrocontroller(id, name, location, ipAddress);
         System.out.println(Color.successMessage("Microcontroller added successfully!"));
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }

   public ClimateRecord createRegister(int id, String microcontrollerId, double temperature, double humidity, double pressure) throws Exception {
      try {
         ClimateRecord record = microcontrollerService.createRegister(microcontrollerId, id, temperature, humidity, pressure);
         System.out.println(Color.successMessage("Register created successfully!"));
         return record;
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
      }
   }

   public ClimateRecord updateRegister(int id, String microcontrollerId, double temperature, double humidity, double pressure) throws Exception {
      try {
         ClimateRecord newRecord = new ClimateRecord(id, microcontrollerId, temperature, humidity, pressure);
         microcontrollerService.updateRegister(id, newRecord);
         System.out.println(Color.successMessage("Register updated successfully!"));
         return newRecord;
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
         return null;
      }
   }

   public void printMicrocontroller(String id) throws Exception {
      try {
         microcontrollerService.printMicrocontroller(id);
      } catch (Exception e) {
         System.out.println(Color.errorMessage(e.getMessage()));
      }
   }
}
