package model.DAO;

import model.entities.ClimateRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import datastructures.HashTable;
import datastructures.HashTable.Node;

public class ClimateRecordDAO {
   private final String FILE = "src/database/records.txt";
   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
   private HashTable<ClimateRecord> hashTable;

   public ClimateRecordDAO(HashTable<ClimateRecord> hashTableRecords) {
      this.hashTable = hashTableRecords;
   }

   // operations

   public void save(ClimateRecord record, boolean isLog) throws Exception {
      if (exists(record.getId())) {
         throw new Exception("Climate record " + record.getId() + " already exists");
      } else {
         hashTable.insert(record.getId(), record);
         LogDAO.saveLogHashTable("Inserted key " + record.getId() + " with value " + record.toString(), "INSERT", "HASH TABLE");
         
         if (isLog) {
         LogDAO.saveLogHashTableStructure(hashTable);
         }

         writeFile(record);
      }
   }

   public ClimateRecord getRecordById(int id) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      }
      ClimateRecord record = hashTable.search(id).value;
      LogDAO.saveLogHashTableStructure(hashTable);
      return record; 
   }

   public void removeRecord(int id, boolean isLog) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      } else {
         hashTable.remove(id);
         LogDAO.saveLogHashTable("Removed key " + id, "REMOVE", "HASH TABLE");

         if (isLog) {
         LogDAO.saveLogHashTableStructure(hashTable);
         }

         writeFile(null);
         
      }
   }

   public void updateRecord(int id, double temperature, double humidity, double pressure, boolean isLog) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      } else {
         ClimateRecord record = hashTable.search(id).value;
         record.setTemperature(temperature);
         record.setHumidity(humidity);
         record.setPressure(pressure);

         hashTable.insert(id, record);

         if (isLog) {
            LogDAO.saveLogHashTableStructure(hashTable);
         }

         writeFile(record);
      }
   }

   public void getAllRecords() {
      HashTable<ClimateRecord>.Node<ClimateRecord>[] nodes = hashTable.print();
      for (HashTable<ClimateRecord>.Node<ClimateRecord> node : nodes) {
         if (node != null) {
            System.out.println(node.value.toString(false));
         }
      }
      LogDAO.saveLogHashTableStructure(hashTable);
   }

   public int getRecordCount() {
      return hashTable.getOccupied();
   }

   public boolean exists(int id) {
      return hashTable.exists(id);
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
