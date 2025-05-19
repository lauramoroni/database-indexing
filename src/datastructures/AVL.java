package datastructures;

import model.DAO.LogDAO;
import utils.Color;

@SuppressWarnings("unchecked")
public class AVL<T> {
   private Node<T> root;

   public AVL() {
      this.root = null;
   }

   public Node<T> getRoot() {
      return root;
   }

   public void setRoot(Node<T> root) {
      this.root = root;
   }

   // operations

   public void insert(int key, T t) {
      root = insert(root, key, t);
   }
   private Node<T> insert(Node<T> node, int key, T t) {

      if (node == null) {
         return new Node<>(key, t);
      }
      if (key < node.getKey()) {
         node.setLeft(insert(node.getLeft(), key, t));
      } else if (key > node.getKey()) {
         node.setRight(insert(node.getRight(), key, t));
      } else {
         throw new IllegalArgumentException(Color.errorMessage("Duplicate key: " + key));
      }

      node.setHeight(1 + biggest(height(node.getLeft()), height(node.getRight())));

      return node;
   }

   public Node<T> remove(int key) {
      return root = remove(root, key);
   }
   private Node<T> remove(Node<T> node, int key) {
      if (node == null) {
         return node;
      }
      // 1°: node without child
      if (node.getLeft() == null && node.getRight() == null) {
         return null;
      }
      // 2°: node with one right child
      else if (node.getLeft() == null) {
         Node<T> temp = node;
         node = temp.getRight();
         temp = null;
      } 
      // 3°: node with one left child
      else if (node.getRight() == null) {
         Node<T> temp = node;
         node = temp.getLeft();
         temp = null;
      }
      // 4°: node with two children
      else {
         Node<T> temp = smallest(node.getRight());
         node.setKey(temp.getKey());
         node.setValue(temp.getValue());

         temp.setKey(key);

         node.setRight(remove(node.getRight(), key));
      }

      if (node == null) {
         return node;
      }

      node = verifyBalance(node);

      return node;
   }

   public void inOrder() {
      inOrder(root);
   }
   private void inOrder(Node<T> node) {
      if (node != null) {
         inOrder(node.getLeft());
         System.out.println(Color.infoMessage(node.getValue().toString()));
         inOrder(node.getRight());
      }
   }

   // search for a node
   public Node<T> search(int key) {
      return search(root, key);
   }
   private Node<T> search(Node<T> node, int key) {
      if (node == null || node.getKey() == key) {
         return node;
      }
      if (key < node.getKey()) {
         return search(node.getLeft(), key);
      } else {
         return search(node.getRight(), key);
      }
   }

   // exists
   public boolean exists(int key) {
      return exists(root, key);
   }
   private boolean exists(Node<T> node, int key) {
      if (node == null) {
         return false;
      }
      if (node.getKey() == key) {
         return true;
      }
      if (key < node.getKey()) {
         return exists(node.getLeft(), key);
      } else {
         return exists(node.getRight(), key);
      }
   }

   // balance the tree
   public int getBalance(Node<T> node) {
      if (node == null) {
         return 0;
      }
      return height(node.getLeft()) - height(node.getRight());
   }

   private Node<T> verifyBalance(Node<T> node) {
      // update height
      node.setHeight(1 + biggest(height(node.getLeft()), height(node.getRight())));

      int balance = getBalance(node);
      int leftBalance = getBalance(node.getLeft());
      int rightBalance = getBalance(node.getRight());

      // rotação esquerda simples
      if (balance < -1 && rightBalance <= 0) {

         LogDAO.saveLog("RES " + node.getKey(), "AVL ROTATION");

         return leftRotation(node);
      }
      // rotação direita simples
      if (balance > 1 && leftBalance >= 0) {

         LogDAO.saveLog("RDS " + node.getKey(), "AVL ROTATION");

         return rightRotation(node);
      }
      // rotação dupla esquerda
      if (balance < -1 && rightBalance > 0) {
         node.setRight(rightRotation(node.getRight()));

         LogDAO.saveLog("RDE " + node.getKey(), "AVL ROTATION");

         return leftRotation(node);
      }
      // rotação dupla direita
      if (balance > 1 && leftBalance < 0) {
         node.setLeft(leftRotation(node.getLeft()));

         LogDAO.saveLog("RDD " + node.getKey(), "AVL ROTATION");

         return rightRotation(node);
      }

      return node;
   }

   private Node<T> leftRotation(Node<T> node) {
      Node<T> newRoot = node.getRight();
      Node<T> temp = newRoot.getLeft();

      newRoot.setLeft(node);
      node.setRight(temp);

      node.setHeight(1 + biggest(height(node.getLeft()), height(node.getRight())));
      newRoot.setHeight(1 + biggest(height(newRoot.getLeft()), height(newRoot.getRight())));

      return newRoot;
   }

   private Node<T> rightRotation(Node<T> node) {
      Node<T> newRoot = node.getLeft();
      Node<T> temp = newRoot.getRight();

      newRoot.setRight(node);
      node.setLeft(temp);

      node.setHeight(1 + biggest(height(node.getLeft()), height(node.getRight())));
      newRoot.setHeight(1 + biggest(height(newRoot.getLeft()), height(newRoot.getRight())));

      return newRoot;
   }

   // utils

   private int height(Node<T> node) {
      if (node == null) {
         return 0;
      }
      return node.getHeight();
   }

   private int biggest(int a, int b) {
      return (a > b) ? a : b;
   }

   private Node<T> smallest(Node<T> node) {
      Node<T> temp = node;

      while (temp.getLeft() != null) {
         temp = temp.getLeft();
      }

      return temp;
   }

   public int size() {
      return size(root);
   }
   private int size(Node<T> node) {
      if (node == null) {
         return 0;
      }
      return 1 + size(node.getLeft()) + size(node.getRight());
   }
}
