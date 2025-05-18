package model.DAO;

import java.io.*;
import java.util.*;
import model.entities.Microcontroller;
import utils.Location;

public class MicrocontrollerDAO {
   private final String FILE = "src/database/microcontrollers.dat";
   private Map<String, Microcontroller> microcontrollers;

   public MicrocontrollerDAO() {
      this.microcontrollers = new HashMap<>();
   }

   public void save(Microcontroller microcontroller) {
      microcontrollers.put(microcontroller.getId(), microcontroller);
      if (!exists(microcontroller.getId())) {
         try (FileOutputStream fos = new FileOutputStream(FILE, true)) {
            String data = microcontroller.getId() + "," +
                  microcontroller.getName() + "," +
                  microcontroller.getLocation() + "," +
                  microcontroller.getIpAddress() + "\n";
            fos.write(data.getBytes());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public boolean exists(String id) {
      try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
         String line;
         while ((line = reader.readLine()) != null) {
            if (line.startsWith(id + ","))
               return true;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

   public Microcontroller getMicrocontroller(String id) {
      try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
         String line;
         while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equals(id)) {
               return new Microcontroller(data[0], data[1], Location.valueOf(data[2]), data[3]);
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }
}
