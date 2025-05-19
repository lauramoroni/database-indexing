package service;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import datastructures.AVL;
import model.DAO.ClimateRecordDAO;
import model.DAO.LogDAO;
import model.DAO.MicrocontrollerDAO;
import utils.Location;

public class MicrocontrollerService {
   private MicrocontrollerDAO microcontrollerDAO;
   private ClimateRecordDAO climateRecordDAO;

   public MicrocontrollerService(AVL<Microcontroller> avlMicrocontrollers, AVL<ClimateRecord> avlRecords) {
      this.microcontrollerDAO = new MicrocontrollerDAO(avlMicrocontrollers);
      this.climateRecordDAO = new ClimateRecordDAO(avlRecords);
   }

   public void addMicrocontroller(String name, Location location, String ipAddress) throws Exception {
      Microcontroller microcontroller = new Microcontroller(name, location, ipAddress);
      try {
         microcontrollerDAO.save(microcontroller);
         LogDAO.saveLog(microcontroller.toString(), "BD INSERT");
      } catch (Exception e) {
         e.getMessage();
      }
   }

   public ClimateRecord createRegister(int microcontrollerId, double temperature, double humidity,
         double pressure) throws Exception {
      if (!microcontrollerDAO.exists(microcontrollerId)) {
         throw new Exception("Microcontroller " + microcontrollerId + " not found");
      }

      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(microcontrollerId);

      ClimateRecord record = new ClimateRecord(microcontrollerId, temperature, humidity, pressure);

      // linked list logic
      if (microcontroller.getHead() == null) {
         microcontroller.setHead(record);
         microcontroller.setTail(record);
      } else {
         microcontroller.getTail().setNext(record);
         microcontroller.setTail(record);
      }

      microcontroller.setRecordCount(microcontroller.getRecordCount() + 1);
      microcontroller.getRecordsIds()[microcontroller.getRecordCount()] = record.getId();
      LogDAO.saveLog(record.toLog(), "BD INSERT");

      try {
         climateRecordDAO.save(record);
         LogDAO.saveLog("Inserted record " + record.getId() + " in AVL tree", "AVL INSERT");
      } catch (Exception e) {
         e.getMessage();
      }

      return record;
   }

   //public ClimateRecord updateRegister(int id, ClimateRecord newRecord) throws Exception {
//
   //   if (!microcontrollerDAO.exists(newRecord.getMicrocontrollerId())) {
   //      throw new Exception("Microcontroller " + newRecord.getMicrocontrollerId() + " not found");
   //   }
   //   Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(newRecord.getMicrocontrollerId());
//
   //   ClimateRecord current = microcontroller.getHead();
   //   while (current != null) {
   //      if (current.getId() == id && current.getMicrocontrollerId().equals(newRecord.getMicrocontrollerId())) {
   //         current.setTemperature(newRecord.getTemperature());
   //         current.setHumidity(newRecord.getHumidity());
   //         current.setPressure(newRecord.getPressure());
   //         return current;
   //      }
   //      current = current.getNext();
   //   }
//
   //   LogDAO.saveLog(newRecord.toString(), "BD UPDATE");
//
   //   // update the AVL tree
   //   LogDAO.saveLog("Updated record " + newRecord.getId() + " in AVL tree", "AVL UPDATE");
//
   //   return null;
   //}
   public ClimateRecord[] getRecordsByMicrocontrollerId(int microcontrollerId) throws Exception {
      if (!microcontrollerDAO.exists(microcontrollerId)) {
         throw new Exception("Microcontroller " + microcontrollerId + " not found");
      }

      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(microcontrollerId);
      
      int[] recordsIds = microcontroller.getRecordsIds();
      System.out.println(microcontrollerId + " " + microcontroller.getRecordCount());

      ClimateRecord[] records = new ClimateRecord[microcontroller.getRecordCount()];
      for (int i = 0; i < microcontroller.getRecordCount(); i++) {
         records[i] = climateRecordDAO.getRecordById(recordsIds[i]);
      }
      return records;
   }

   public void printMicrocontroller(int id) throws Exception {
      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(id);
      if (microcontroller != null) {
         System.out.println(microcontroller.toString());
      } else {
         throw new Exception("Microcontroller " + id + " not found");
      }
   }

}
