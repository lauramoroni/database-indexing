package model.entities;

import java.time.LocalDateTime;

public class ClimateRecord {
   int id;
   String microcontrollerId;
   LocalDateTime timestamp;
   double temperature;
   double humidity;
   double pressure;
   ClimateRecord next;
}
