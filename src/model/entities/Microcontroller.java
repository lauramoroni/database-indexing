package model.entities;

import utils.Location;

public class Microcontroller {
   public String id;
   public int[] recordsIds;
   public String name;
   public Location location;
   public String ipAddress;
   public ClimateRecord head;
   public ClimateRecord tail;
   public int recordCount;

   public Microcontroller(String id, String name, Location location, String ipAddress) {
      this.id = id;
      this.recordsIds = new int[100];
      this.name = name;
      this.location = location;
      this.ipAddress = ipAddress;
      this.head = null;
      this.tail = null;
      this.recordCount = 0;
   }

   public String getId() {
      return id;
   }

   public int getRecordCount() {
      return recordCount;
   }

   @Override
   public String toString() {
      return String.format(
            "Microcontroller [%s]\n" +
                  "├─ Name: %s\n" +
                  "├─ Location: %s\n" +
                  "├─ IP: %s\n" +
                  "└─ Climate Records: %d",
            id,
            name,
            location,
            ipAddress,
            recordCount);
   }
}
