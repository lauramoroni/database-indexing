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
         tail.next = newNode;
         tail = newNode;
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


}
