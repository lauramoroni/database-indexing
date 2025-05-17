package model.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import utils.Color;

public class ClimateRecord {
   int id;
   String microcontrollerId;
   LocalDateTime timestamp;
   double temperature;
   double humidity;
   double pressure;
   ClimateRecord next;

   public ClimateRecord(int id, String microcontrollerId, double temperature, double humidity, double pressure) {
      this.id = id;
      this.microcontrollerId = microcontrollerId;
      this.timestamp = LocalDateTime.now();
      this.temperature = temperature;
      this.humidity = humidity;
      this.pressure = pressure;
      this.next = null;
   }

   public ClimateRecord(int id, String microcontrollerId, LocalDateTime timestamp, double temperature, double humidity,
         double pressure) {
      this.id = id;
      this.microcontrollerId = microcontrollerId;
      this.timestamp = timestamp;
      this.temperature = temperature;
      this.humidity = humidity;
      this.pressure = pressure;
      this.next = null;
   }

   public int getId() {
      return id;
   }

   public String getMicrocontrollerId() {
      return microcontrollerId;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public double getTemperature() {
      return temperature;
   }

   public double getHumidity() {
      return humidity;
   }

   public double getPressure() {
      return pressure;
   }

   public void setTemperature(double temperature) {
      this.temperature = temperature;
   }

   public void setHumidity(double humidity) {
      this.humidity = humidity;
   }

   public void setPressure(double pressure) {
      this.pressure = pressure;
   }

   public ClimateRecord getNext() {
      return next;
   }

   public void setNext(ClimateRecord next) {
      this.next = next;
   }

   @Override
   public String toString() {
      return String.format(Color.infoMessage("Climate Register #%d\n" +
                  "├─ Microcontroller: %s\n" +
                  "├─ Date/Time: %s\n" +
                  "├─ Temperature: %.1f°C\n" +
                  "├─ Humidity: %.1f%%\n" +
                  "└─ Pressure: %.1f hPa"), 
            id,
            microcontrollerId,
            timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            temperature,
            humidity,
            pressure);
   }
}
