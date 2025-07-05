package datastructures;

import model.DAO.LogDAO;

@SuppressWarnings({"unchecked", "hiding"})
public class HashTable<T> {
   private Node<T>[] table;
   private int size;
   private int occupied = 0;

   public class Node<T> {
      int key;
      T value;
      Node<T> next;

      public Node(int key, T value) {
         this.key = key;
         this.value = value;
         this.next = null;
      }
   }

   public HashTable(int size) {
      if (size <= 0) {
         throw new IllegalArgumentException("Size must be greater than zero");
      }
      this.size = size;
      table = new Node[size];
   }

   

   public int getSize() {
      return size;
   }



   public int getOccupied() {
      return occupied;
   }



   private int nextPrime(int n) {
      while (!isPrime(n)) {
         n++;
      }
      return n;
   }

   private boolean isPrime(int n) {
      for (int i = 2; i <= Math.sqrt(n); i++) {
         if (n % i == 0) {
            return false;
         }
      }
      return true;
   }

   private int hash(int key) {
      return key % size;
   }

   public int hashCollision(int index, int i) {
      // colisao por endereçamento aberto
      return (hash(i) + index) % size;
   }

   public void insert(int key, T value) {
      int index = hash(key);

      if (table[index] == null) {
         // if the index is empty, insert the new node
         table[index] = new Node<>(key, value);
         occupied++;
      } else {
         if (table[index].key == key) {
            table[index].value = value; // update value
            LogDAO.saveLogHashTable("Updated value for key " + key, "UPDATE", "HASH TABLE");
            return;
         }
         
         // handle collision
         LogDAO.saveLogHashTable("Collision occurred while inserting key " + key, "COLLISION", "HASH TABLE");

         int i = 1;
         while(table[hashCollision(index, i)] != null) {
            if (table[hashCollision(index, i)].key == key) {
               table[hashCollision(index, i)].value = value; // update value

               LogDAO.saveLogHashTable("Updated value for key " + key, "UPDATE", "HASH TABLE");

               return;
            }
            i++;
         }

         table[hashCollision(index, i)] = new Node<>(key, value);
         occupied++;
      }
      LogDAO.saveLogHashTable("Inserted key " + key + " with value " + value, "INSERT", "HASH TABLE");
   }

   public Node<T> search(int key) {
      int h = hash(key);

      while (table[h] != null) {
         if (table[h].key == key) {
            LogDAO.saveLogHashTable("Found key " + key + " with value " + table[h].value, "SEARCH", "HASH TABLE");
            return table[h]; // found
         }
         h = hashCollision(h, 1); // handle collision
      }  
      return null;
   }

   void remove(int key) {
      // implement removal logic
   }

   void print() {
      Node<T> node;

      for(int i = 0; i < size; i++) {
         node = table[i];
         if (node != null) {
            System.out.print("Index " + i + ": ");
            while (node != null) {
               System.out.print("[" + node.key + ": " + node.value + "] ");
               node = node.next;
            }
            System.out.println();
         } else {
            System.out.println("Index " + i + ": empty");
         }
      }
   }

   public static void main(String[] args) {
      HashTable<String> hashTable = new HashTable<>(10);
      hashTable.insert(1, "One");
      hashTable.insert(2, "Two");
      hashTable.insert(12, "Twelve"); // Collision with key 2
      hashTable.insert(3, "Three");

      hashTable.print();

      HashTable<String>.Node<String> searchResult = hashTable.search(12);
      if (searchResult != null) {
         System.out.println("Found: [" + searchResult.key + ": " + searchResult.value + "]");
      } else {
         System.out.println("Not found");
      }
   }
}
