package model.DAO;

import datastructures.AVL;
import model.entities.Microcontroller;

public class MicrocontrollerDAO {
   // private final String FILE = "src/database/microcontrollers.txt";
   private AVL<Microcontroller> avl;

   public MicrocontrollerDAO(AVL<Microcontroller> avl) {
      this.avl = avl;
      // populateAVL();
   }

   public void save(Microcontroller microcontroller) throws Exception {
      if (exists(microcontroller.getId())) {
         throw new Exception("Microcontroller " + microcontroller.getId() + " already exists");
      }

      avl.insert(microcontroller.getId(), microcontroller);
   }

   public boolean exists(int id) {
      return avl.exists(id);
   }

   public Microcontroller getMicrocontroller(int id) {
      return avl.search(id).getValue();
   }

   public void update(Microcontroller microcontroller) {
      avl.remove(microcontroller.getId());
      avl.insert(microcontroller.getId(), microcontroller);
   }

   // public void writeToFile() {
   // try {
   // File file = new File(FILE);
   // try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
   // for (int i = 0; i < avl.size(); i++) {
   // Microcontroller microcontroller = avl.search(i).getValue();
   // if (microcontroller == null) {
   // continue;
   // }
   // writer.write(microcontroller.getId() + "," +
   // microcontroller.getName() + "," +
   // microcontroller.getLocation() + "," +
   // microcontroller.getIpAddress());
   // writer.newLine();
   // }
   // }
   // } catch (IOException e) {
   // e.printStackTrace();
   // }
   // }
   //
   // public void populateAVL() {
   // try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
   // String line;
   // while ((line = reader.readLine()) != null) {
   // String[] data = line.split(",");
   // int id = Integer.parseInt(data[0]);
   // String name = data[1];
   // Location location = Location.valueOf(data[2]);
   // String ipAddress = data[3];
   //
   // Microcontroller microcontroller = new Microcontroller(id, name, location,
   // ipAddress);
   // avl.insert(id, microcontroller);
   // }
   // } catch (IOException e) {
   // e.printStackTrace();
   // }
   // }
}
