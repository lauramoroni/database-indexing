package service;

import model.entities.User;

import java.util.Map;
import java.util.HashMap;

public class UserService {
   private final Map<String, User> users;

   public UserService() {
      this.users = new HashMap<>();
   }

   public void addUser(String id, String name, String password) throws Exception {
      if (users.containsKey(id)) {
         throw new Exception("User with ID " + id + " already exists.");
      }

      User user = new User(id, name, password);
      users.put(id, user);
   }

   public User login(String id, String password) throws Exception {
      User user = users.get(id);

      if (user == null) {
         throw new Exception("User not found.");
      }

      if (!user.getPassword().equals(password)) {
         throw new Exception("Invalid password.");
      }

      return user;
   }

   public User getUser(String id) throws Exception {
      User user = users.get(id);
      if (user == null) {
         throw new Exception("User not found.");
      }
      return user;
   }

   public boolean exists(String id) {
      return users.containsKey(id);
   }

   public void printUser(String id) throws Exception {
      User user = users.get(id);
      if (user == null) {
         throw new Exception("User not found.");
      }

      System.out.println(user.toString());
   }
}
