package model.DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import datastructures.HashTable;
import model.entities.Microcontroller;

public class MicrocontrollerDAO {
   private final String FILE = "src/database/microcontrollers.txt";
   private HashTable<Microcontroller> hashTable;

   public MicrocontrollerDAO(HashTable<Microcontroller> hashTableMicrocontrollers) {
      this.hashTable = hashTableMicrocontrollers;
   }

   public void save(Microcontroller microcontroller, boolean isLog) throws Exception {
      if (exists(microcontroller.getId())) {
         throw new Exception("Microcontroller " + microcontroller.getId() + " already exists");
      }

      hashTable.insert(microcontroller.getId(), microcontroller);
      LogDAO.saveLogHashTable("Inserted key " + microcontroller.getId() + " with value " + microcontroller.toString(true), "INSERT", "HASH TABLE");
      if (isLog) {
         LogDAO.saveLogHashTableStructure(hashTable);
      }
      writeFile(microcontroller);
   }

   public boolean exists(int id) {
      return hashTable.exists(id);
   }

   public Microcontroller getMicrocontroller(int id) {
      return hashTable.search(id).value;
   }

   public void update(Microcontroller microcontroller) {
      hashTable.insert(microcontroller.getId(), microcontroller);
   }

   public void printMicrocontrollers() {
      HashTable<Microcontroller>.Node<Microcontroller>[] nodes = hashTable.print();
      for (HashTable<Microcontroller>.Node<Microcontroller> node : nodes) {
         if (node != null) {
            System.out.println(node.value.toString(false));
         }
      }
   }

   public int countMicrocontrollers() {
      return hashTable.getOccupied();
   }

   public void writeFile(Microcontroller microcontroller) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
         writer.write(microcontroller.getId() + "," +
               microcontroller.getName() + "," +
               microcontroller.getLocation() + "," +
               microcontroller.getIpAddress() + "," +
               microcontroller.getRecordCount());
         writer.newLine();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
