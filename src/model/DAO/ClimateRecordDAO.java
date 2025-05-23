package model.DAO;

import model.entities.ClimateRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import datastructures.AVL;

public class ClimateRecordDAO {
   private final String FILE = "src/database/records.txt";
   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
   private AVL<ClimateRecord> avl;

   public ClimateRecordDAO(AVL<ClimateRecord> avl) {
      this.avl = avl;
   }

   // operations

   public void save(ClimateRecord record) throws Exception {
      if (exists(record.getId())) {
         throw new Exception("Climate record " + record.getId() + " already exists");
      } else {
         avl.insert(record.getId(), record);
         LogDAO.saveLogAVL("Inserted record " + record.getId() + " in AVL tree", "INSERT", avl.search(record.getId()).getHeight(), "RECORD");
         LogDAO.logAVLTreeStructure(avl);
         writeFile(record);
      }
   }

   public ClimateRecord getRecordById(int id) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      }
      return avl.search(id).getValue(); // search(id) returns a node (AVL search), and get the value ('search' LL)
   }

   public void removeRecord(int id) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      } else {
         LogDAO.saveLogAVL("Removed record " + id + " from AVL tree", "REMOVE", avl.search(id).getHeight(), "RECORD");
         avl.remove(id);
         LogDAO.logAVLTreeStructure(avl);

         writeFile(null);
         
      }
   }

   public void updateRecord(int id, double temperature, double humidity, double pressure) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      } else {
         ClimateRecord record = avl.search(id).getValue();
         record.setTemperature(temperature);
         record.setHumidity(humidity);
         record.setPressure(pressure);
         LogDAO.saveLogAVL("Updated record " + id + " in AVL tree", "UPDATE", avl.search(id).getHeight(), "RECORD");

         writeFile(record);
      }
   }

   public void getAllRecords() {
      avl.inOrder();
   }

   public int getRecordCount() {
      return avl.size();
   }

   public boolean exists(int id) {
      return avl.exists(id);
   }

   public void writeFile(ClimateRecord record) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
         writer.write(record.getId() + "," +
               record.getMicrocontrollerId() + "," +
               record.getTemperature() + "," +
               record.getHumidity() + "," +
               record.getPressure() + "," +
               record.getTimestamp().format(formatter));
         writer.newLine();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
