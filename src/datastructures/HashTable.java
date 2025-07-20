package datastructures;

import model.DAO.LogDAO;

@SuppressWarnings({ "unchecked", "hiding" })
public class HashTable<T> {
   private Node<T>[] table;
   private int size;
   private int occupied = 0;

   private int resizes = 0;
   private int collisions = 0;

   private final int TAM_BASE = 3;

   public class Node<T> {
      public int key;
      public T value;
      public Node<T> next;

      public Node(int key, T value) {
         this.key = key;
         this.value = value;
         this.next = null;
      }
   }

   public HashTable(int capacity) {
      if (capacity <= 0) {
         throw new IllegalArgumentException("Capacity must be greater than zero");
      }
      int power2 = nearestPower2(capacity);
      int prime = largestPrimeSmallerThan(power2);

      // use the largest prime number smaller than the nearest power of 2
      size = Math.max(prime, TAM_BASE);

      table = new Node[size];
   }

   public int getSize() {
      return size;
   }

   public int getOccupied() {
      return occupied;
   }

   public int getCollisions() {
      return collisions;
   }

   public int getResizes() {
      return resizes;
   }

   public double getLoadFactor() {
      return (double) occupied / size;
   }

   public Node<T>[] getTable() {
      return table;
   }

   public int nearestPower2(int n) {
      if (n < 1) {
         throw new IllegalArgumentException("n must be greater than 0");
      }
      int power = 1;
      while (power < n) {
         power *= 2;
      }
      return power;
   }

   private int largestPrimeSmallerThan(int n) {
      for (int i = n - 1; i > 1; i--) {
         if (isPrime(i)) {
            return i;
         }
      }
      return 2;
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
      // metodo da divisão
      return (key & 0x7FFFFFFF) % size;
      // metodo da multiplicação
      //double A = (Math.sqrt(5) - 1) / 2; // constante de Knuth
      //return (int) (size * ((key * A) % 1)) & 0x7FFFFFFF; 
   }

   private void testLoadFactor() {
      double loadFactor = getLoadFactor();

      if (loadFactor > 1.5) {
         resize(2 * size);
      } else if (loadFactor < 0.5 && size > TAM_BASE) {
         resize(size / 2);
      }
   }

   public int hashCollision(int index, int i) {
      // linear
      //return (hash(i) + index) % size;
      // quadratic
      //  h(x, k) = (hash(x) + c1k + c2k2) mod m, 0 ≤ k ≤ m-1
      int c1 = 1; // constante para o termo linear
      int c2 = 1; // constante para o termo quadrático
      return (hash(index) + c1 * i + c2 * i * i) % size;
   }

   // tratamento de colisão por encadeamento separado
   public void insert(int key, T value) {
      int index = hash(key);
      Node<T> currentNode = table[index];

      while (currentNode != null) {
         if (currentNode.key == key) {
            currentNode.value = value;
            LogDAO.saveLogHashTable("Updated key " + key + " with value " + value.toString(), "UPDATE", "HASH TABLE");
            return;
         }
         currentNode = currentNode.next;
      }

      Node<T> newNode = new Node<>(key, value);
      newNode.next = table[index];
      table[index] = newNode;
      occupied++;

      // contabiliza colisão se a lista já tinha pelo menos 1 elemento
      if (table[index].next != null) {
         collisions++;
         LogDAO.saveLogHashTable("N° " + collisions + " | Collision detected for key " + key, "COLLISION",
               "HASH TABLE");
      }

      testLoadFactor(); 
   }

   private void reinsert(int key, T value) {
      int index = hash(key);
      Node<T> newNode = new Node<>(key, value);
      newNode.next = table[index];
      table[index] = newNode;
      occupied++;
      if (table[index].next != null) {
         collisions++;
         LogDAO.saveLogHashTable("N° " + collisions + " | Collision detected for key " + key, "COLLISION", "HASH TABLE");
      }
   }

   private void resize(int capacity) {

      resizes++;
      Node<T>[] oldTable = table;

      int newSize = largestPrimeSmallerThan(nearestPower2(capacity));
      this.size = newSize;
      this.table = new Node[newSize];
      this.occupied = 0; // reset occupied count

      for (Node<T> node : oldTable) {
         Node<T> currentNode = node;
         while (currentNode != null) {
            reinsert(currentNode.key, currentNode.value); // reinsert nodes into the new table
            currentNode = currentNode.next;
         }
      }

      LogDAO.saveLogHashTable("N° " + resizes + " | Resized hash table to new size " + size, "RESIZE", "HASH TABLE");

      LogDAO.saveLogHashTable("Load factor after resize: " + getLoadFactor(), "RESIZE", "HASH TABLE");
   }

   public Node<T> search(int key) {
      int index = hash(key);
      Node<T> currentNode = table[index];

      while (currentNode != null) {
         if (currentNode.key == key) {
            return currentNode;
         }
         currentNode = currentNode.next;
      }
      return null;
   }

   public void remove(int key) {
      int index = hash(key);
      Node<T> currentNode = table[index];
      Node<T> previousNode = null;

      while (currentNode != null) {
         if (currentNode.key == key) {
            if (previousNode == null) {
               // first node in the list
               table[index] = currentNode.next;
            } else {
               // middle or last node in the list
               previousNode.next = currentNode.next;
            }
            occupied--;
            testLoadFactor();
            return;
         }
         previousNode = currentNode;
         currentNode = currentNode.next;
      }
      throw new IllegalArgumentException("Key not found: " + key);
   }

   public Node<T>[] print() {
      Node<T>[] nodes = new Node[occupied];
      int count = 0;
      for (int i = 0; i < size; i++) {
         Node<T> currentNode = table[i];
         while (currentNode != null) {
            nodes[count++] = currentNode;
            currentNode = currentNode.next;
         }
      }
      return nodes;
   }

   public boolean exists(int key) {
      int index = hash(key);
      Node<T> currentNode = table[index];

      while (currentNode != null) {
         if (currentNode.key == key) {
            return true;
         }
         currentNode = currentNode.next;
      }
      return false;
   }

   public String toMessage() {
      return String.format("%d | %d | %.2f | %d | %d", size, occupied, getLoadFactor(), collisions, resizes);
   }
}
