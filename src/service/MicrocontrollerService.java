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
   private ClimateRecordDAO climateRecordDAO = new ClimateRecordDAO();
   private AVL avl;

   public MicrocontrollerService() {
      this.microcontrollerDAO = new MicrocontrollerDAO();
      this.avl = new AVL();
   }

   public void addMicrocontroller(String id, String name, Location location, String ipAddress) throws Exception {
      if (microcontrollerDAO.exists(id)) {
         throw new Exception("Microcontroller " + id + " already exists");
      }

      Microcontroller microcontroller = new Microcontroller(id, name, location, ipAddress);

      microcontrollerDAO.save(microcontroller);

      LogDAO.saveLog(microcontroller.toString(), "BD INSERT");
   }

   public ClimateRecord createRegister(String microcontrollerId, int id, double temperature, double humidity,
         double pressure) throws Exception {
      if (!microcontrollerDAO.exists(microcontrollerId)) {
         throw new Exception("Microcontroller " + microcontrollerId + " not found");
      }

      if (climateRecordDAO.exists(id, microcontrollerId)) {
         throw new Exception("Climate record " + id + " already exists for microcontroller " + microcontrollerId);
      }

      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(microcontrollerId);

      ClimateRecord record = new ClimateRecord(id, microcontrollerId, temperature, humidity, pressure);

      // linked list logic
      if (microcontroller.head == null) {
         microcontroller.head = record;
         microcontroller.tail = record;
      } else {
         microcontroller.tail.setNext(record);
         microcontroller.tail = record;
      }

      microcontroller.recordsIds[microcontroller.recordCount] = record.getId();
      microcontroller.recordCount++;
      
      climateRecordDAO.save(record);

      LogDAO.saveLog(record.toString(), "BD INSERT");

      // update the AVL tree
      avl.insert(record.getId(), record);
      LogDAO.saveLog("Inserted record  " + record.getId() + " into AVL tree", "AVL INSERT");
      
      return record;
   }

   public ClimateRecord updateRegister(int id, ClimateRecord newRecord) throws Exception {

      if (!microcontrollerDAO.exists(newRecord.getMicrocontrollerId())) {
         throw new Exception("Microcontroller " + newRecord.getMicrocontrollerId() + " not found");
      }
      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(newRecord.getMicrocontrollerId());

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

      climateRecordDAO.update(newRecord);

      LogDAO.saveLog(newRecord.toString(), "BD UPDATE");

      // update the AVL tree
      LogDAO.saveLog("Updated record " + newRecord.getId() + " in AVL tree", "AVL UPDATE");

      return null;
   }

   public void printMicrocontroller(String id) throws Exception {
      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(id);
      if (microcontroller != null) {
         System.out.println(microcontroller.toString());
      } else {
         throw new Exception("Microcontroller " + id + " not found");
      }
   }

}
