package protocol;

import java.time.LocalDateTime;

import datastructures.HashTable;
import model.entities.ClimateRecord;
import model.entities.Microcontroller;
import model.entities.User;
import protocol.huffman.HuffmanTree;
import utils.Location;

public class Message {
   public String content;
   public HuffmanTree huffmanTree;

   java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

   
   // construtor comprime o conteúdo da mensagem
   // getters descomprimem o conteúdo

   // === cliente -> servidor ===
   // ex: Show all records, Show hash table details
   public Message(String content) {
      this.content = content;

      this.huffmanTree = new HuffmanTree();

      huffmanTree.buildTree(content);
      //huffmanTree.printCodes();
      
      this.content = huffmanTree.compress(content);
   }

   // ex: create record
   public Message(String content, ClimateRecord record) {
      // destrinchar o objeto
      this.content = content + "-" + record.toMessage();

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   public Message(String content, int microcontrollerId, double temperature, double humidity, double pressure) {
      this.content = content + "-" + microcontrollerId + "-" + temperature + "-" + humidity + "-" + pressure;

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }


   // ex: Show record by ID, Show records by Microcontroller ID, Show microcontroller by ID, Quantity of records, update record, Remove record by ID
   public Message(String content, int number) {
      this.content = content + "-" + number;

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   // ex: create microcontroller
   public Message(String content, Microcontroller microcontroller) {
      // destrinchar o objeto
      this.content = content + "-" + microcontroller.toMessage();

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   public Message(String content, String name, Location location, String ipAddress) {
      this.content = content + "-" + name + "-" + location.toString() + "-" + ipAddress;

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   public Message(String content, User user) {
      // destrinchar o objeto
      this.content = content + "-" + user.toMessage();

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   public Message(String content, String userId, String userName, String userPassword) {
      this.content = content + "-" + userId + "-" + userName + "-" + userPassword;

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   public Message(String content, String id, String password) {
      this.content = content + "-" + id + "-" + password;

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   // servidor -> cliente
   // ex: Show all records, Show records by Microcontroller ID
   public Message(String content, ClimateRecord[] records) {
      this.content = content;

      for (ClimateRecord record : records) {
         this.content +=  "-" + record.toMessage();
      }

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   // ex: Show all microcontrollers
   public Message(String content, Microcontroller[] microcontrollers) {
      this.content = content;

      for (Microcontroller microcontroller : microcontrollers) {
         this.content += "-" + microcontroller.toMessage();
      }

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   // ex: Hash table details
   public Message(String content, HashTable hashTable) {
      this.content = content + "-" + hashTable.toMessage();

      this.huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(this.content);
      //huffmanTree.printCodes();

      this.content = huffmanTree.compress(this.content);
   }

   // getters para descomprimir o conteúdo
   public String getContent() {
      return huffmanTree.decompress(content);
   }

   public ClimateRecord getClimateRecord() {
      this.content = huffmanTree.decompress(content);

      String[] parts = this.content.split("-");

      //parts[0] é o conteúdo
      return new ClimateRecord(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), LocalDateTime.parse(parts[3], formatter), Double.parseDouble(parts[4].replace(',', '.')), Double.parseDouble(parts[5].replace(',', '.')), Double.parseDouble(parts[6].replace(',', '.')));
   }

   public ClimateRecord[] getClimateRecords() {
      this.content = huffmanTree.decompress(content);

      String[] parts = this.content.split("-");
      ClimateRecord[] records = new ClimateRecord[(parts.length - 1) / 6];

      int recordCount = 0;
      for (int i = 1; i < (parts.length - 1); i += 6) {
         records[recordCount++] = new ClimateRecord(Integer.parseInt(parts[i]), Integer.parseInt(parts[i + 1]), LocalDateTime.parse(parts[i + 2], formatter), Double.parseDouble(parts[i + 3].replace(',', '.')), Double.parseDouble(parts[i + 4].replace(',', '.')), Double.parseDouble(parts[i + 5].replace(',', '.')));
      }

      return records;
   }

   public Microcontroller getMicrocontroller() {
      this.content = huffmanTree.decompress(content);

      String[] parts = this.content.split("-");

      // parts[0] é o conteúdo
      return new Microcontroller(parts[1], Location.valueOf(parts[2]), parts[3]);
   }

   // ex: get microcontroller count, get record count
   public int getCount() {
      this.content = huffmanTree.decompress(content);
      String[] parts = this.content.split("-");
      return Integer.parseInt(parts[1]);
   }

   // ex: user
   public User getUser() {
      this.content = huffmanTree.decompress(content);

      String[] parts = this.content.split("-");

      // parts[0] é o conteúdo
      return new User(parts[1], parts[2], parts[3]);
   }

   public static void main(String[] args) {
      Microcontroller microcontroller = new Microcontroller("MC1", Location.FORTALEZA, "v1.0");
      ClimateRecord record = new ClimateRecord(1, microcontroller.getId(), 25.5, 60.0, 1013.0);
      ClimateRecord record2 = new ClimateRecord(2, microcontroller.getId(), 26.0, 65.0, 1012.0);
      ClimateRecord record3 = new ClimateRecord(3, microcontroller.getId(), 24.5, 55.0, 1011.0);

      Message createRecord = new Message("create record", record);
      Message getAll = new Message("Show all records");
      Message getById = new Message("Show record by ID", 2);
      Message getByMicrocontrollerId = new Message("Show records by Microcontroller ID", 1);
      Message createMicrocontroller = new Message("create microcontroller", microcontroller);
      Message getAllReturn = new Message("Show all records", new ClimateRecord[]{record, record2, record3});
      Message getAllMicrocontrollers = new Message("Show all microcontrollers", new Microcontroller[]{microcontroller});
      Message hashTableDetails = new Message("Hash table details", new HashTable(10));
      
      System.out.println("Compressed create record: " + createRecord.content);
      System.out.println("Compressed get all records: " + getAll.content);
      System.out.println("Compressed get record by ID: " + getById.content);
      System.out.println("Compressed get records by Microcontroller ID: " + getByMicrocontrollerId.content);
      System.out.println("Compressed create microcontroller: " + createMicrocontroller.content);
      System.out.println("Compressed get all records (return): " + getAllReturn.content);
      System.out.println("Compressed get all microcontrollers: " + getAllMicrocontrollers.content);
      System.out.println("Compressed hash table details: " + hashTableDetails.content);

      System.out.println("Decompressed create record: " + createRecord.huffmanTree.decompress(createRecord.content));
      System.out.println("Decompressed get all records: " + getAll.huffmanTree.decompress(getAll.content));
      System.out.println("Decompressed get record by ID: " + getById.huffmanTree.decompress(getById.content));
      System.out.println("Decompressed get records by Microcontroller ID: " + getByMicrocontrollerId.huffmanTree.decompress(getByMicrocontrollerId.content));
      System.out.println("Decompressed create microcontroller: " + createMicrocontroller.huffmanTree.decompress(createMicrocontroller.content));
      System.out.println("Decompressed get all records (return): " + getAllReturn.huffmanTree.decompress(getAllReturn.content));
      System.out.println("Decompressed get all microcontrollers: " + getAllMicrocontrollers.huffmanTree.decompress(getAllMicrocontrollers.content));
      System.out.println("Decompressed hash table details: " + hashTableDetails.huffmanTree.decompress(hashTableDetails.content));
   }
}
