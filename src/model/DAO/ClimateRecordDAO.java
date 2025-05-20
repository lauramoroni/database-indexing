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
         LogDAO.saveLogAVL("Inserted record " + record.getId() + " in AVL tree", "INSERT", avl.search(record.getId()).getHeight(), "REGISTER");
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
         LogDAO.saveLogAVL("Removed record " + id + " from AVL tree", "REMOVE", avl.search(id).getHeight(), "REGISTER");
         avl.remove(id);
         LogDAO.logAVLTreeStructure(avl);

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
