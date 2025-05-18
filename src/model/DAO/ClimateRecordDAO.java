package model.DAO;

import model.entities.ClimateRecord;

import java.io.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClimateRecordDAO {
   private final String FILE = "src/database/records.dat";
   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

   public void save(ClimateRecord record) {
      if (!exists(record.getId(), record.getMicrocontrollerId())) {
         try (FileOutputStream fos = new FileOutputStream(FILE, true)) {
            String data = record.getId() + "," +
                  record.getMicrocontrollerId() + "," +
                  record.getTimestamp().format(formatter) + "," +
                  record.getTemperature() + "," +
                  record.getHumidity() + "," +
                  record.getPressure() + "\n";
            fos.write(data.getBytes());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public boolean exists(int id, String microcontrollerId) {
      try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
         String line;
         while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (Integer.parseInt(parts[0]) == id &&
                  parts[1].equals(microcontrollerId)) {
               return true;
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

   public void update(ClimateRecord updatedRecord) {
      try {
         List<String> lines = Files.readAllLines(Paths.get(FILE));
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (String line : lines) {
               String[] parts = line.split(",");

               int recordId = Integer.parseInt(parts[0]);
               String microId = parts[1];

               if (recordId == updatedRecord.getId() && microId.equals(updatedRecord.getMicrocontrollerId())) {
                  String newLine = updatedRecord.getId() + "," +
                        updatedRecord.getMicrocontrollerId() + "," +
                        updatedRecord.getTimestamp().format(formatter) + "," +
                        updatedRecord.getTemperature() + "," +
                        updatedRecord.getHumidity() + "," +
                        updatedRecord.getPressure();
                  writer.write(newLine);
               } else {
                  writer.write(line);
               }
               writer.newLine();
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
