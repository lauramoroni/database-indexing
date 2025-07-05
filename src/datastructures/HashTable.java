package datastructures;

import java.util.Random;

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
      return Math.abs(key) % size;
   }

   private void testLoadFactor() {
      double loadFactor = getLoadFactor();

      if (loadFactor >= 1.0) {
         resize(size * 2);
      }
   }

   public int hashCollision(int index, int i) {
      return (hash(i) + index) % size;
   }

   public void insert(int key, T value) {
      int index = hash(key);
      Node<T> currentNode = table[index];

      // Percorre a lista para verificar se a chave já existe
      while (currentNode != null) {
         if (currentNode.key == key) {
            currentNode.value = value; // Atualiza o valor se a chave for encontrada
            LogDAO.saveLogHashTable("Updated key " + key + " with value " + value, "UPDATE", "HASH TABLE");
            return;
         }
         currentNode = currentNode.next;
      }

      // Insere o novo nó no início da lista
      Node<T> newNode = new Node<>(key, value);
      newNode.next = table[index];
      table[index] = newNode;
      occupied++;

      // Contabiliza colisão se a lista já tinha pelo menos 1 elemento
      if (table[index].next != null) {
         collisions++;
         LogDAO.saveLogHashTable("N° " + collisions + " | Collision detected for key " + key, "COLLISION",
               "HASH TABLE");
      }

      LogDAO.saveLogHashTable("Inserted key " + key + " with value " + value, "INSERT", "HASH TABLE");

      testLoadFactor(); // Verifica a necessidade de redimensionamento
   }

   private void reinsert(int key, T value) {
      int index = hash(key);
      Node<T> newNode = new Node<>(key, value);
      newNode.next = table[index];
      table[index] = newNode;
      occupied++;
      if (table[index].next != null) {
         collisions++;
      }
   }

   private void resize(int capacity) {

      resizes++;
      Node<T>[] oldTable = table;

      int newSize = largestPrimeSmallerThan(nearestPower2(capacity));
      this.size = newSize;
      this.table = new Node[newSize];
      this.occupied = 0; // reset occupied count
      this.collisions = 0; // reset collisions count

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
            LogDAO.saveLogHashTable("Found key " + key + " at index " + index, "SEARCH", "HASH TABLE");
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
            LogDAO.saveLogHashTable("Removed key " + key, "REMOVE", "HASH TABLE");
            testLoadFactor();
            return;
         }
         previousNode = currentNode;
         currentNode = currentNode.next;
      }
      throw new IllegalArgumentException("Key not found: " + key);
   }

   public void print() {
      for (int i = 0; i < size; i++) {
         System.out.print("Index " + i + ":");
         Node<T> node = table[i];
         if (node == null) {
            System.out.println(" empty");
         } else {
            while (node != null) {
               System.out.print(" --> [" + node.key + ": " + node.value + "]");
               node = node.next;
            }
            System.out.println();
         }
      }
   }

   public static void main(String[] args) {
      System.out.println("Initializing HashTable with a small capacity to force resizing...");

      // Começa com uma capacidade pequena (resulta em tamanho 13) para garantir
      // redimensionamentos.
      HashTable<Integer> hashTable = new HashTable<>(10);
      Random random = new Random();
      int numInsertions = 100;

      System.out.println("Initial table size: " + hashTable.getSize());
      System.out.println("Performing " + numInsertions + " random insertions...");
      System.out.println("----------------------------------------------------");

      for (int i = 0; i < numInsertions; i++) {
         // Gera chaves aleatórias entre 0 e 999.
         // A aleatoriedade garante a ocorrência de colisões.
         int key = random.nextInt(1000);
         int value = random.nextInt(1000);

         // Para simplificar, o valor será a própria chave.
         hashTable.insert(key, value);
      }

      System.out.println("\nAll " + numInsertions + " insertions are complete.");
      System.out.println("----------------------------------------------------");

      // O método print() foi modificado para exibir as estatísticas finais.
      // Ele mostrará a tabela final e os contadores de colisões e redimensionamentos.
      hashTable.print();

      // Exemplo de busca para verificar se a tabela funciona após as operações
      System.out.println("\n--- Sanity Check: Searching for a key ---");
      // Tenta buscar por uma chave que pode ou não ter sido inserida
      int searchKey = 500;
      HashTable<Integer>.Node<Integer> result = hashTable.search(searchKey);
      if (result != null) {
         System.out.println("Found key " + searchKey + " with value: " + result.value);
      } else {
         System.out.println("Key " + searchKey + " was not found in the table.");
      }

      LogDAO.saveLogHashTableStructure(hashTable);
   }
}
