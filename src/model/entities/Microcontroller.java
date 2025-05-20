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
   private ClimateRecord head;
   private ClimateRecord tail;
   private int recordCount;

   public Microcontroller(String name, Location location, String ipAddress) {
      this.id = ++nextId;
      this.recordsIds = new int[100];
      this.name = name;
      this.location = location;
      this.ipAddress = ipAddress;
      this.head = null;
      this.tail = null;
      this.recordCount = 0;
   }

   public Microcontroller(int id, String name, Location location, String ipAddress) {
      id = id;
      this.recordsIds = new int[100];
      this.name = name;
      this.location = location;
      this.ipAddress = ipAddress;
      this.head = null;
      this.tail = null;
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

   public ClimateRecord getHead() {
      return head;
   }

   public ClimateRecord getTail() {
      return tail;
   }

   public void setHead(ClimateRecord head) {
      this.head = head;
   }

   public void setTail(ClimateRecord tail) {
      this.tail = tail;
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

   @Override
   public String toString() {
      return String.format(Color.infoMessage("Microcontroller #%d\n" +
            "├─ Name: %s\n" +
            "├─ Location: %s\n" +
            "├─ IP Address: %s\n" +
            "└─ Record Count: %d\n"), id, name, location, ipAddress, recordCount);
   }

   public String toLog() {
      return String.format("Microcontroller[%d] Name:%s Location:%s IP:%s Records:%d", id, name, location, ipAddress, recordCount);
   }
}
