package model.DAO;

import model.entities.ClimateRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import datastructures.HashTable;

public class ClimateRecordDAO {
   private final String FILE = "src/database/records.txt";
   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
   private HashTable<ClimateRecord> hashTable;

   public ClimateRecordDAO(HashTable<ClimateRecord> hashTableRecords) {
      this.hashTable = hashTableRecords;
   }

   // operations

   public void save(ClimateRecord record) throws Exception {
      if (exists(record.getId())) {
         throw new Exception("Climate record " + record.getId() + " already exists");
      } else {
         hashTable.insert(record.getId(), record);
         LogDAO.saveLogHashTableStructure(hashTable);
         writeFile(record);
      }
   }

   public ClimateRecord getRecordById(int id) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      }
      return hashTable.search(id).value; 
   }

   public void removeRecord(int id) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      } else {
         hashTable.remove(id);
         LogDAO.saveLogHashTableStructure(hashTable);

         writeFile(null);
         
      }
   }

   public void updateRecord(int id, double temperature, double humidity, double pressure) throws Exception {
      if (!exists(id)) {
         throw new Exception("Record " + id + " not found");
      } else {
         ClimateRecord record = hashTable.search(id).value;
         record.setTemperature(temperature);
         record.setHumidity(humidity);
         record.setPressure(pressure);

         writeFile(record);
      }
   }

   public void getAllRecords() {
      hashTable.print();
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
