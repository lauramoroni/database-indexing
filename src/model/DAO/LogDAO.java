package model.DAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import datastructures.AVL;
import datastructures.HashTable;
import datastructures.Node;

public class LogDAO {
   private static final String FILE = "src/database/log.txt";
   private static final String AVL_FILE = "src/database/avl.txt";
   private static final String HASH_TABLE_FILE = "src/database/hash_table.txt";

   public static void saveLog(String message, String type, String operation) {
      LocalDateTime now = LocalDateTime.now();
      String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

      // Formatação com largura fixa para melhor leitura
      String logEntry = String.format(
            "%-20s | %-12s | %-10s | %s%n",
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

   // Logs for AVL operations

   public static void saveLogAVL(String message, String operation, String typeAVL) {
      LocalDateTime now = LocalDateTime.now();
      String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

      // Formatação com largura fixa para melhor leitura
      String logEntry = String.format(
            "%-20s | %-12s | %-10s | %s%n",
            formattedDate,
            typeAVL,
            "[" + operation + "]",
            message);

      try (BufferedWriter bw = new BufferedWriter(new FileWriter(AVL_FILE, true))) {
         bw.write(logEntry);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static <T> void logAVLTreeStructure(AVL<T> avl) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(AVL_FILE, true))) {
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

   // Logs for HashTable operations
   public static void saveLogHashTable(String message, String operation, String type) {
      LocalDateTime now = LocalDateTime.now();
      String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

      // Formatação com largura fixa para melhor leitura
      String logEntry = String.format(
            "%-20s | %-12s | %-10s | %s%n",
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

   public static <T> void saveLogHashTableStructure(HashTable<T> hashTable) {
      // Clear the hash table log file before writing new structure
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(HASH_TABLE_FILE))) {
         writer.write("");
      } catch (IOException e) {
         e.printStackTrace();
      }
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(HASH_TABLE_FILE, true))) {
         for (int i = 0; i < hashTable.getSize(); i++) {
            StringBuilder lineBuilder = new StringBuilder();
            lineBuilder.append(i).append(": ");

            HashTable<T>.Node<T> currentNode = hashTable.getTable()[i];

            if (currentNode == null) {
               lineBuilder.append("[empty]");
            } else {
               while (currentNode != null) {
                  lineBuilder.append("--> [key=").append(currentNode.key)
                        .append(", value=").append(currentNode.value).append("] ");

                  currentNode = currentNode.next;
               }
            }
            writer.write(lineBuilder.toString() + "\n");
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
