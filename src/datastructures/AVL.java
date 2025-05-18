package datastructures;

import model.entities.ClimateRecord;
import utils.Color;

public class AVL {
   Node root;
   int balanceFactor;

   public AVL() {
      this.root = null;
      this.balanceFactor = 0;
   }

   public Node getRoot() {
      return root;
   }

   public void setRoot(Node root) {
      this.root = root;
   }

   public int getBalanceFactor() {
      return balanceFactor;
   }

   public void setBalanceFactor(int balanceFactor) {
      this.balanceFactor = balanceFactor;
   }

   // operations

   public void insert(int key, ClimateRecord recordAddress) {
      root = insert(root, key, recordAddress);
   }
   private Node insert(Node node, int key, ClimateRecord recordAddress) {
      
      if (node == null) {
         return new Node(key, recordAddress);
      }
      if (key < node.getKey()) {
         node.setLeft(insert(node.getLeft(), key, recordAddress));
      } else if (key > node.getKey()) {
         node.setRight(insert(node.getRight(), key, recordAddress));
      } else {
         throw new IllegalArgumentException(Color.errorMessage("Duplicate key: " + key));
      }

      node.setHeight(1 + biggest(height(node.getLeft()), height(node.getRight())));

      return node;
   }

   // search for a node
   public Node search(int key) {
      return search(root, key);
   }
   private Node search(Node node, int key) {
      if (node == null || node.getKey() == key) {
         return node;
      }
      if (key < node.getKey()) {
         return search(node.getLeft(), key);
      } else {
         return search(node.getRight(), key);
      }
   }

   // balance the tree

   private int height(Node node) {
      if (node == null) {
         return 0;
      }
      return node.getHeight();
   }

   private int biggest(int a, int b) {
      return (a > b) ? a : b;
   }
}
