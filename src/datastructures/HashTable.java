package datastructures;

@SuppressWarnings({"unchecked", "hiding"})
public class HashTable<T> {
   private Node<T>[] table;
   private int size;

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
      this.size = size;
      table = new Node[size];
   }

   void insert(int key, T value) {
      // implement insertion logic
   }

   Node<T> search(int key) {
      // implement search logic
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
}
