package controller;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import service.MicrocontrollerService;
import utils.Location;

public class MicrocontrollerController {
   private MicrocontrollerService microcontrollerService;

   public MicrocontrollerController() {
      this.microcontrollerService = new MicrocontrollerService();
   }

   public void addMicrocontroller(String id, String name, Location location, String ipAddress) {
      Microcontroller microcontroller = new Microcontroller(id, name, location, ipAddress);
      microcontrollerService.addMicrocontroller(microcontroller);
   }

   public ClimateRecord createRegister(int id, String microcontrollerId, double temperature, double humidity, double pressure) {
      ClimateRecord record = new ClimateRecord(id, microcontrollerId, temperature, humidity, pressure);

      return microcontrollerService.createRegister(microcontrollerId, record);
   }

   public ClimateRecord updateRegister(int id, String microcontrollerId, double temperature, double humidity, double pressure) {
      ClimateRecord newRecord = new ClimateRecord(id, microcontrollerId, temperature, humidity, pressure);

      return microcontrollerService.updateRegister(id, newRecord);
   }

   public void printMicrocontroller(String id) {
      microcontrollerService.printMicrocontroller(id);
   }
}
