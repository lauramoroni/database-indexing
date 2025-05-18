package datastructures;

import model.entities.ClimateRecord;

public class Node {
   int key;
   Node left, right;
   int height;
   ClimateRecord recordAddress; // node value

   // create root node
   public Node(int key, ClimateRecord recordAddress) {
      this.key = key;
      this.recordAddress = recordAddress;
      this.left = null;
      this.right = null;
      this.height = 1; 
   }

   public int getKey() {
      return key;
   }

   public void setKey(int key) {
      this.key = key;
   }

   public Node getLeft() {
      return left;
   }

   public void setLeft(Node left) {
      this.left = left;
   }

   public Node getRight() {
      return right;
   }

   public void setRight(Node right) {
      this.right = right;
   }

   public int getHeight() {
      return height;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public ClimateRecord getRecordAddress() {
      return recordAddress;
   }

   public void setRecordAddress(ClimateRecord recordAddress) {
      this.recordAddress = recordAddress;
   }

}
