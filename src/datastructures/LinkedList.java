package datastructures;

public class LinkedList<T> {
   private static class Node<T> {
      T data;
      Node<T> next;

      public Node(T data) {
         this.data = data;
         this.next = null;
      }
   }
   
   private Node<T> head;
   private Node<T> tail;
   private int size;

   public Node<T> getHead() {
      return head;
   }
   public void setHead(Node<T> head) {
      this.head = head;
   }
   public Node<T> getTail() {
      return tail;
   }
   public void setTail(Node<T> tail) {
      this.tail = tail;
   }

   public LinkedList() {
      head = null;
      tail = null;
      size = 0;
   }

   public void insert(T data) {
      Node<T> newNode = new Node<>(data);
      if (head == null) {
         head = newNode;
         tail = newNode;
      } else {
         Node<T> current = head;
         while (current.next != null) {
            current = current.next;
         }
         current.next = newNode;
      }
      size++;
   }

   public void remove(int key) {
      if (head == null) {
         return;
      }

      if (head.data.equals(key)) {
         head = head.next;
         size--;
         return;
      }

      Node<T> current = head;
      while (current.next != null && !current.next.data.equals(key)) {
         current = current.next;
      }

      if (current.next != null) {
         current.next = current.next.next;
         size--;
         if (current.next == null) {
            tail = current;
         }
      }
   }

   public Node<T> search(Node<T> node, T key) {
      Node<T> current, before = null;
      T temp;

      for (current = node; current != null; current = current.next) {
         if (current.data.equals(key)) {
            if (current != head && before != null) {
               temp = before.data;
               before.data = current.data;
               current.data = temp;
            }
            return current;
         }
         before = current;
      }
      return null;
   }

   public static void main(String[] args) {
      LinkedList<Integer> list = new LinkedList<>();
      list.insert(1);
      list.insert(2);
      list.insert(3);
      list.insert(4);
      list.insert(5);

      // print
      for (Node<Integer> current = list.getHead(); current != null; current = current.next) {
         System.out.print(current.data + " ");
      }

      // teste metodo de autoajuste transposição
      System.out.println("\nSearching for 5...");
      Node<Integer> foundNode = list.search(list.getHead(), 5);
      if (foundNode != null) {
         System.out.println("Found: " + foundNode.data);
      } else {
         System.out.println("Not found");
      }

      // print after search
      for (Node<Integer> current = list.getHead(); current != null; current = current.next) {
         System.out.print(current.data + " ");
      }
   }
}
