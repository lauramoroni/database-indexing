package model.entities;

import utils.Color;
import utils.Location;

public class Microcontroller {
   private static int nextId = 0;
   private int id = 0;
   private int[] recordsIds;
   private String name;
   private Location location;
   private String ipAddress;
   private int recordCount;

   public Microcontroller(String name, Location location, String ipAddress) {
      this.id = ++nextId;
      this.recordsIds = new int[1000];
      this.name = name;
      this.location = location;
      this.ipAddress = ipAddress;
      this.recordCount = 0;
   }

   public int getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public Location getLocation() {
      return location;
   }

   public String getIpAddress() {
      return ipAddress;
   }

   public int getRecordCount() {
      return recordCount;
   }

   public void setRecordCount(int recordCount) {
      this.recordCount = recordCount;
   }

   public int[] getRecordsIds() {
      return recordsIds;
   }

   public void setRecordsIds(int[] recordsIds) {
      this.recordsIds = recordsIds;
   }

   public void incrementRecord(int id) {
      this.recordCount++;
      this.recordsIds[recordCount - 1] = id;
   }

   public void decrementRecord(int id) {
      for (int i = 0; i < recordCount; i++) {
         if (recordsIds[i] == id) {
            recordsIds[i] = recordsIds[recordCount - 1];
            recordCount--;
            break;
         }
      }
   }

   public String toString() {
      return toString(true);
   }

   public String toString(boolean log) {
      if (log) {
         return String.format("Microcontroller{id=%d, name='%s', location=%s, ipAddress='%s', recordCount=%d}",
               id, name, location, ipAddress, recordCount);
      } else {
         return String.format(Color.infoMessage("Microcontroller #%d\n" +
               "├─ Name: %s\n" +
               "├─ Location: %s\n" +
               "├─ IP Address: %s\n" +
               "└─ Record Count: %d\n"), id, name, location, ipAddress, recordCount);
      }
   }

   public String toMessage() {
      return String.format("%d-%s-%s-%s", id, name, location, ipAddress);
   }

}
