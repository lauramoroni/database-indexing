package service;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;

import datastructures.HashTable;
import datastructures.LinkedList;
import model.DAO.ClimateRecordDAO;
import model.DAO.LogDAO;
import model.DAO.MicrocontrollerDAO;
import utils.Location;

public class MicrocontrollerService {
   private MicrocontrollerDAO microcontrollerDAO;
   private ClimateRecordDAO climateRecordDAO;
   private LinkedList<ClimateRecord> linkedList;

   public MicrocontrollerService(HashTable<Microcontroller> hashTableMicrocontrollers, HashTable<ClimateRecord> hashTableRecords, LinkedList<ClimateRecord> linkedList) {
      this.microcontrollerDAO = new MicrocontrollerDAO(hashTableMicrocontrollers);
      this.climateRecordDAO = new ClimateRecordDAO(hashTableRecords);
      this.linkedList = linkedList;
   }

   public void addMicrocontroller(String name, Location location, String ipAddress) throws Exception {
      Microcontroller microcontroller = new Microcontroller(name, location, ipAddress);
      try {
         LogDAO.saveLog("Inserting microcontroller " + microcontroller.getId(), "MC", "INSERT");
         microcontrollerDAO.save(microcontroller);
      } catch (Exception e) {
         e.getMessage();
      }
   }

   public ClimateRecord createRecord(int microcontrollerId, double temperature, double humidity,
         double pressure) throws Exception {
      if (!microcontrollerDAO.exists(microcontrollerId)) {
         throw new Exception("Microcontroller " + microcontrollerId + " not found");
      }

      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(microcontrollerId);

      ClimateRecord record = new ClimateRecord(microcontrollerId, temperature, humidity, pressure);

      // linked list logic
      linkedList.insert(record);

      microcontroller.incrementRecord(record.getId());

      LogDAO.saveLog("Inserting record " + record.getId() + " for microcontroller " + microcontrollerId, "CR", "INSERT");

      try {
         climateRecordDAO.save(record);
      } catch (Exception e) {
         e.getMessage();
      }

      return record;
   }

   public void updateRecord(int id, double temperature, double humidity, double pressure) throws Exception {
      if (!climateRecordDAO.exists(id)) {
         throw new Exception("Record " + id + " not found");
      }

      ClimateRecord oldRecord = climateRecordDAO.getRecordById(id);
      oldRecord.setTemperature(temperature);
      oldRecord.setHumidity(humidity);
      oldRecord.setPressure(pressure);

      linkedList.remove(id);
      linkedList.insert(oldRecord);

      LogDAO.saveLog("Updating record " + id + " for microcontroller " + oldRecord.getMicrocontrollerId(), "CR", "BD UPDATE");

      try {
         climateRecordDAO.updateRecord(id, temperature, humidity, pressure);
      } catch (Exception e) {
         e.getMessage();
      }
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

   public int getMicrocontrollerCount() {
      return microcontrollerDAO.countMicrocontrollers();
   }

}