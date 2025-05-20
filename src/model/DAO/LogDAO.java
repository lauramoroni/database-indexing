package model.DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogDAO {
   private static final String FILE = "src/database/log.txt";

   public static void saveLog(String message, String type, String operation) {
      LocalDateTime now = LocalDateTime.now();
      String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

      // Formatação com largura fixa para melhor leitura
      String logEntry = String.format(
            "%-20s | %-4s | %-15s | %s%n",
            formattedDate,
            type,
            "[" + operation + "]",
            message);

      try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
         bw.write(logEntry);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
