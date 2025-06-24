package datastructures;

public class Node<T> {
   int key;
   Node<T> left, right;
   int height;
   T value; // ClimateRecord or Microcontroller

   // create root node
   public Node(int key, T value) {
      this.key = key;
      this.value = value;
      this.left = null;
      this.right = null;
      this.height = 0; 
   }

   public int getKey() {
      return key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public Node<T> getLeft() {
      return left;
   }

   public void setLeft(Node<T> left) {
      this.left = left;
   }

   public Node<T> getRight() {
      return right;
   }

   public void setRight(Node<T> right) {
      this.right = right;
   }

   public int getHeight() {
      return height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public T getValue() {
      return value;
   }

   public void setValue(T value) {
      this.value = value;
   }

}
