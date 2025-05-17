package service;

import java.util.HashMap;
import java.util.Map;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import utils.Color;
import utils.Location;

public class MicrocontrollerService {
   private Map<String, Microcontroller> microcontrollers;

   public MicrocontrollerService() {
      this.microcontrollers = new HashMap<>();
   }

   public void addMicrocontroller(String id, String name, Location location, String ipAddress) throws Exception {
      if (microcontrollers.containsKey(id)) {
            throw new Exception("Microcontroller " + id + " already exists");
      }
      microcontrollers.put(id, new Microcontroller(id, name, location, ipAddress));
   }

   public ClimateRecord createRegister(String microcontrollerId, ClimateRecord record) throws Exception {

      if (!microcontrollers.containsKey(microcontrollerId)) {
         throw new Exception("Microcontroller " + microcontrollerId + " not found");
      }

      Microcontroller microcontroller = microcontrollers.get(microcontrollerId);

      if (microcontroller.head == null) {
         microcontroller.head = record;
         microcontroller.tail = record;
      } else {
         microcontroller.tail.setNext(record);
         microcontroller.tail = record;
      }

      microcontroller.recordsIds[microcontroller.recordCount] = record.getId();
      microcontroller.recordCount++;

      return record;
   }

   public ClimateRecord updateRegister(int id, ClimateRecord newRecord) throws Exception {

      if (!microcontrollers.containsKey(newRecord.getMicrocontrollerId())) {
         throw new Exception("Microcontroller " + newRecord.getMicrocontrollerId() + " not found");
      }
      Microcontroller microcontroller = microcontrollers.get(newRecord.getMicrocontrollerId());

      ClimateRecord current = microcontroller.head;
      while (current != null) {
         if (current.getId() == id && current.getMicrocontrollerId().equals(newRecord.getMicrocontrollerId())) {
            current.setTemperature(newRecord.getTemperature());
            current.setHumidity(newRecord.getHumidity());
            current.setPressure(newRecord.getPressure());
            return current;
         }
         current = current.getNext();
      }
      return null;
      
   }

   public void printMicrocontroller(String id) throws Exception {
      Microcontroller microcontroller = microcontrollers.get(id);
      if (microcontroller != null) {
         System.out.println(microcontroller.toString());
      } else {
         throw new Exception("Microcontroller " + id + " not found");
      }
   }

}
