package protocol;

import model.entities.ClimateRecord;
import model.entities.User;
import service.MicrocontrollerService;
import service.UserService;
import utils.Location;

public class MessageHandler {
   private MicrocontrollerService microcontrollerService;
   private UserService userService;

   public MessageHandler(MicrocontrollerService microcontrollerService) {
      this.microcontrollerService = microcontrollerService;
   }

   public MessageHandler(UserService userService) {
      this.userService = userService;
   }

   public Message handleMessage(Message message, boolean isLog) {
      // descompress the message content
      String decompressedContent = message.getContent();

      String[] parts = decompressedContent.split("-");
      String operation = parts[0];

      switch (operation) {
         case "CREATE_MC":
            // Handle create microcontroller
            String name = parts[2];
            String location = parts[3];
            String ipAddress = parts[4];
            try {
               microcontrollerService.addMicrocontroller(name, Location.fromString(location), ipAddress, isLog);
               return new Message("Microcontroller created successfully");
            } catch (Exception e) {
               return new Message("Error adding microcontroller: " + e.getMessage());
            }
         case "CREATE_CR":
            int microcontrollerId = Integer.parseInt(parts[2].trim().replace(",", "."));
            double temperature = Double.parseDouble(parts[4].trim().replace(",", "."));
            double humidity = Double.parseDouble(parts[5].trim().replace(",", "."));
            double pressure = Double.parseDouble(parts[6].trim().replace(",", "."));
            try {
               ClimateRecord createdRecord = microcontrollerService.createRecord(microcontrollerId, temperature, humidity, pressure, isLog);
               return new Message("Climate record created successfully", createdRecord);
            } catch (Exception e) {
               return new Message("Error creating climate record: " + e.getMessage());
            }
            case "UPDATE_CR":
            int recordId = Integer.parseInt(parts[1]);
            double newTemperature = Double.parseDouble(parts[4].trim().replace(",", "."));
            double newHumidity = Double.parseDouble(parts[5].trim().replace(",", "."));
            double newPressure = Double.parseDouble(parts[6].trim().replace(",", "."));
            try {
               microcontrollerService.updateRecord(recordId, newTemperature, newHumidity, newPressure, isLog);
               return new Message("Climate record updated successfully");
            } catch (Exception e) {
               return new Message("Error updating climate record: " + e.getMessage());
            }
            case "GET_CR_BY_MC":
            int mcId = Integer.parseInt(parts[2]);
            try {
               ClimateRecord[] records = microcontrollerService.getRecordsByMicrocontrollerId(mcId);
               return new Message("Records retrieved successfully", records);
            } catch (Exception e) {
               return new Message("Error getting records by microcontroller ID: " + e.getMessage());
            }
            case "GET_CR_COUNT":
            try {
               int recordCount = microcontrollerService.getRecordCount();
               return new Message("Record count retrieved successfully", recordCount);
            } catch (Exception e) {
               return new Message("Error getting record count: " + e.getMessage());
            }
         case "GET_MC_COUNT":
            try {
               int microcontrollerCount = microcontrollerService.getMicrocontrollerCount();
               return new Message("Microcontroller count retrieved successfully", microcontrollerCount);
            } catch (Exception e) {
               return new Message("Error getting microcontroller count: " + e.getMessage());
            }
         case "CREATE_USER":
            String userId = parts[1];
            String userName = parts[2];
            String userPassword = parts[3];
            try {
               userService.addUser(userId, userName, userPassword);
               return new Message("User created successfully");
            } catch (Exception e) {
               return new Message("Error adding user: " + e.getMessage());
            }
            case "LOGIN":
            String loginId = parts[1];
            String loginPassword = parts[2];
            try {
               User userLogged = userService.login(loginId, loginPassword);
               return new Message("User logged in successfully", userLogged);
            } catch (Exception e) {
               return new Message("Error logging in user: " + e.getMessage());
            }
         case "GET_ALL":
            try {
               userService.getAllRecords();
               return new Message("All records retrieved successfully");
            } catch (Exception e) {
               return new Message("Error getting all records: " + e.getMessage());
            }
         case "GET_CR_BY_ID":
            int recordById = Integer.parseInt(parts[1]);
            try {
               ClimateRecord recordByIdUser = userService.getRecordById(recordById);
               return new Message("Record retrieved successfully", recordByIdUser);
            } catch (Exception e) {
               return new Message("Error getting climate record by ID: " + e.getMessage());
            }
         case "REMOVE_CR":
            int removeRecordId = Integer.parseInt(parts[1]);
            try {
               userService.removeRecord(removeRecordId, isLog);
               return new Message("Record removed successfully");
            } catch (Exception e) {
               return new Message("Error removing climate record: " + e.getMessage());
            }
         default:
            return new Message("Unknown operation: " + operation);
      }
   }
}
