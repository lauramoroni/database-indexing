package service;

import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import datastructures.HashTable;
import datastructures.LinkedList;
import model.DAO.ClimateRecordDAO;
import model.DAO.LogDAO;
import model.DAO.MicrocontrollerDAO;
import model.DAO.UserDAO;

public class UserService {
   private final UserDAO userDAO;
   private ClimateRecordDAO climateRecordDAO;
   private MicrocontrollerDAO microcontrollerDAO;
   private LinkedList<ClimateRecord> linkedList;

   public UserService(HashTable<Microcontroller> hashTableMicrocontrollers, HashTable<ClimateRecord> hashTableRecords,
         LinkedList<ClimateRecord> linkedList) {
      this.userDAO = new UserDAO();
      this.climateRecordDAO = new ClimateRecordDAO(hashTableRecords);
      this.microcontrollerDAO = new MicrocontrollerDAO(hashTableMicrocontrollers);
      this.linkedList = linkedList;
   }

   public void addUser(String id, String name, String password) throws Exception {
      if (userDAO.exists(id)) {
         throw new Exception("User with ID " + id + " already exists.");
      }

      User user = new User(id, name, password);
      userDAO.save(user);

      LogDAO.saveLog("User " + user.getName() + " created.", "USER", "INFO");
   }

   public User login(String id, String password) throws Exception {
      User user = userDAO.getUser(id);

      if (user == null) {
         throw new Exception("User not found.");
      }

      if (!user.getPassword().equals(password)) {
         throw new Exception("Invalid password.");
      }

      LogDAO.saveLog("User " + user.getName() + " logged in.", "USER", "INFO");

      return user;
   }

   public User getUser(String id) throws Exception {
      User user = userDAO.getUser(id);
      if (user == null) {
         throw new Exception("User not found.");
      }
      return user;
   }

   public boolean exists(String id) {
      return userDAO.exists(id);
   }

   public void printUser(String id) throws Exception {
      User user = userDAO.getUser(id);
      if (user == null) {
         throw new Exception("User not found.");
      }

      System.out.println(user.toString());
   }

   public void getAllRecords() throws Exception {
      climateRecordDAO.getAllRecords();
   }

   public ClimateRecord getRecordById(int id) throws Exception {
      ClimateRecord record = climateRecordDAO.getRecordById(id); // search AVL
      if (record == null) {
         throw new Exception("Record " + id + " not found.");
      }
      return record;
   }

   public void removeRecord(int id) throws Exception {
      if (!climateRecordDAO.exists(id)) {
         throw new Exception("Record " + id + " not found.");
      }

      ClimateRecord record = climateRecordDAO.getRecordById(id);
      Microcontroller microcontroller = microcontrollerDAO.getMicrocontroller(record.getMicrocontrollerId());

      linkedList.remove(id);
      LogDAO.saveLog("Removed record " + id, "CR", "REMOVE");

      microcontroller.decrementRecord(id);

      climateRecordDAO.removeRecord(id);
   }
}
