package model.DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import datastructures.AVL;
import datastructures.Node;

public class LogDAO {
   private static final String FILE = "src/database/log.txt";
   private static final String AVL_FILE = "src/database/avl.txt";

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

   public static void saveLogAVL(String message, String operation, int height, String typeAVL) {
      LocalDateTime now = LocalDateTime.now();
      String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

      // Formatação com largura fixa para melhor leitura
      String logEntry = String.format(
            "%-20s | %-4s | %-10s | %s%n",
            formattedDate,
            typeAVL,
            "[" + operation + "]",
            message, 
            "H: " + height);

      try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVL_FILE, true))) {
         bw.write(logEntry);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static <T> void logAVLTreeStructure(AVL<T> avl) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(AVL_FILE, true))) {
         writer.write("AVL Tree Structure:\n");
         logAVLNode(writer, avl.getRoot(), "", true, "ROOT");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static <T> void logAVLNode(BufferedWriter writer, Node<T> node, String prefix, boolean isLeft,
         String label) throws IOException {
      if (node == null)
         return;

      writer.write(prefix
            + (label.equals("ROOT") ? "└── " : (isLeft ? "├── " : "└── "))
            + label + ": [key=" + node.getKey() + ", h=" + node.getHeight() + "]\n");

      String childPrefix = prefix + (label.equals("ROOT") ? "    " : (isLeft ? "│   " : "    "));
      logAVLNode(writer, node.getLeft(), childPrefix, true, "L");
      logAVLNode(writer, node.getRight(), childPrefix, false, "R");
   }

}
