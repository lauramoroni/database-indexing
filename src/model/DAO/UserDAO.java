package model.DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import model.entities.User;

public class UserDAO {
   private final String FILE = "src/database/users.txt";
   private final File file;

   public UserDAO() {
      this.file = new File(FILE);
   }

   public void save(User user) {
      if (!exists(user.getId())) {
         try (FileOutputStream fos = new FileOutputStream(file, true)) {
            String data = user.getId() + "," +
                  user.getName() + "," +
                  user.getPassword() + "\n";
            fos.write(data.getBytes());
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public boolean exists(String userId) {
      if (!file.exists()) {
         return false;
      }
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
         String line;
         while ((line = reader.readLine()) != null) {
            if (line.startsWith(userId + ","))
               return true;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

   public User getUser(String userId) {
      if (!file.exists()) {
         return null;
      }
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
         String line;
         while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equals(userId)) {
               return new User(data[0], data[1], data[2]);
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;
   }
}
