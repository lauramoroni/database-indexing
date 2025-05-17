package service;

import java.util.HashMap;
import java.util.Map;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import utils.Color;

public class MicrocontrollerService {
   private Map<String, Microcontroller> microcontrollers;

   public MicrocontrollerService() {
      this.microcontrollers = new HashMap<>();
   }

   public void addMicrocontroller(Microcontroller microcontroller) {
      if (microcontrollers.containsKey(microcontroller.getId())) {
            throw new IllegalArgumentException(Color.RED + "Microcontroller already exists: " + microcontroller.getId() + Color.RESET);
      }
      microcontrollers.put(microcontroller.getId(), microcontroller);
   }

   public ClimateRecord createRegister(String microcontrollerId, ClimateRecord record) {

      if (!microcontrollers.containsKey(microcontrollerId)) {
         System.out.println(Color.RED + "Microcontroller not found: " + microcontrollerId + Color.RESET);
         return null;
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

   public ClimateRecord updateRegister(int id, ClimateRecord newRecord) {

      if (!microcontrollers.containsKey(newRecord.getMicrocontrollerId())) {
         System.out.println(Color.RED + "Microcontroller not found: " + newRecord.getMicrocontrollerId() + Color.RESET);
         return null;
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

   public void printMicrocontroller(String id) {
      Microcontroller microcontroller = microcontrollers.get(id);
      if (microcontroller != null) {
         System.out.println(microcontroller.toString());
      } else {
         System.out.println(Color.RED + "Microcontroller not found: " + id + Color.RESET);
      }
   }

}
