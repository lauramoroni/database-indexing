package service;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;

import datastructures.AVL;
import datastructures.LinkedList;
import model.DAO.ClimateRecordDAO;
import model.DAO.LogDAO;
import model.DAO.MicrocontrollerDAO;
import utils.Location;

public class MicrocontrollerService {
   private MicrocontrollerDAO microcontrollerDAO;
   private ClimateRecordDAO climateRecordDAO;
   private LinkedList<ClimateRecord> linkedList;

   public MicrocontrollerService(AVL<Microcontroller> avlMicrocontrollers, AVL<ClimateRecord> avlRecords, LinkedList<ClimateRecord> linkedList) {
      this.microcontrollerDAO = new MicrocontrollerDAO(avlMicrocontrollers);
      this.climateRecordDAO = new ClimateRecordDAO(avlRecords);
      this.linkedList = linkedList;
   }

   public void addMicrocontroller(String name, Location location, String ipAddress) throws Exception {
      Microcontroller microcontroller = new Microcontroller(name, location, ipAddress);
      try {
         LogDAO.saveLog(microcontroller.toLog(), "MC", "BD INSERT");
         microcontrollerDAO.save(microcontroller);
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
      linkedList.insert(record);

      microcontroller.incrementRecord(record.getId());

      LogDAO.saveLog(record.toLog(), "CR", "BD INSERT");

      try {
         climateRecordDAO.save(record);
      } catch (Exception e) {
         e.getMessage();
      }

      return record;
   }

   public ClimateRecord[] getRecordsByMicrocontrollerId(int microcontrollerId) throws Exception {
      if (!microcontrollerDAO.exists(microcontrollerId)) {
         throw new Exception("Microcontroller " + microcontrollerId + " not found");
      }

      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(microcontrollerId);

      ClimateRecord[] records = new ClimateRecord[microcontroller.getRecordCount()];
      for (int i = 0; i < microcontroller.getRecordCount(); i++) {
         records[i] = climateRecordDAO.getRecordById(microcontroller.getRecordsIds()[i]);
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

   public void printAllMicrocontrollers() throws Exception {
      microcontrollerDAO.printMicrocontrollers();
   }

   public int getRecordCount() {
      return climateRecordDAO.getRecordCount();
   }

}