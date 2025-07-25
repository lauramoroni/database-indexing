package protocol.huffman;

public class HuffmanTree {
   public HuffmanNode root;
   public String[] codes;
   private HuffmanHeap heap;
   private char[] characters;
   private int[] frequencies;
   private int uniqueCount;

   public void countCharFrequencie(String message) {
      char[] tempCharArray = new char[message.length()];
      int[] tempFreqArray = new int[message.length()];
      uniqueCount = 0;

      for (int i = 0; i < message.length(); i++) {
         char c = message.charAt(i);
         int index = getIndex(c, tempCharArray, uniqueCount); 

         // se não existe o caracter no array, adiciona
         if (index == -1) {
            tempCharArray[uniqueCount] = c;
            tempFreqArray[uniqueCount] = 1; 
            uniqueCount++;
         } else {
            tempFreqArray[index]++;
         }
      }

      // copia para os arrays finais com o tamanho correto
      characters = new char[uniqueCount];
      frequencies = new int[uniqueCount];
      codes = new String[uniqueCount];
      
      for (int i = 0; i < uniqueCount; i++) {
         characters[i] = tempCharArray[i];
         frequencies[i] = tempFreqArray[i];
      }
   }

   private int getIndex(char c, char[] charArray, int uniqueCount) {
      for (int i = 0; i < uniqueCount; i++) {
         if (charArray[i] == c) {
            return i;
         }
      }
      return -1; // não encontrado
   }

   public HuffmanNode buildTree(String message) {
      // Primeiro conta as frequências
      countCharFrequencie(message);
      
      heap = new HuffmanHeap(uniqueCount);

      for (int i = 0; i < uniqueCount; i++) {
         HuffmanNode node = new HuffmanNode(characters[i], frequencies[i]);
         heap.insert(node);
      }

      heap.buildHeap();

      while (heap.getSize() > 1) {
         HuffmanNode x = heap.removeMin();
         HuffmanNode y = heap.removeMin();

         HuffmanNode newNode = new HuffmanNode(x.frequency + y.frequency, x, y);

         heap.insert(newNode);
      }

      root = heap.removeMin();

      generateCodes(root, "");

      return root;
   }

   private void generateCodes(HuffmanNode node, String code) {
      if (node == null) {
         return;
      }

      if (node.isLeaf()) {
         int index = getIndex(node.character, characters, uniqueCount);
         if (index != -1) {
            codes[index] = code.isEmpty() ? "0" : code; // Garante que não seja vazio
         }
      }
      
      generateCodes(node.left, code + "0");
      generateCodes(node.right, code + "1");
   }

   public String compress(String message) {
      StringBuilder compressed = new StringBuilder();

      for (int i = 0; i < message.length(); i++) {
         char c = message.charAt(i);
         for (int j = 0; j < uniqueCount; j++) {
            if (characters[j] == c) {
               compressed.append(codes[j]);
               break;
            }
         }
      }

      return compressed.toString();
   }

   public String decompress(String compressed) {
      StringBuilder decompressed = new StringBuilder();
      HuffmanNode currentNode = root;

      for (int i = 0; i < compressed.length(); i++) {
         char bit = compressed.charAt(i);

         if (bit == '0') {
            currentNode = currentNode.left;
         } else {
            currentNode = currentNode.right;
         }

         if (currentNode.isLeaf()) {
            decompressed.append(currentNode.character);
            currentNode = root; 
         }
      }

      return decompressed.toString();
   }

   public void printCodes() {
      for (int i = 0; i < uniqueCount; i++) {
         System.out.print(characters[i] + ": " + codes[i] + " | ");
      }
      System.out.println();
   }

   public float getCompressionRatio(String original, String compressed) {
      if (original.isEmpty()) {
         return 0; 
      }
      return (float) compressed.length() / original.length();
   }

   public float getCompressionRatio() {
      StringBuilder original = new StringBuilder();
      for (int i = 0; i < uniqueCount; i++) {
         original.append(characters[i]);
      }
      String compressed = compress(original.toString());
      return getCompressionRatio(original.toString(), compressed);
   }

   public static void main(String[] args) {
      String message = "90.39";
      HuffmanTree huffmanTree = new HuffmanTree();
      huffmanTree.buildTree(message);
      huffmanTree.printCodes();

      String compressed = huffmanTree.compress(message);
      System.out.println("Compressed: " + compressed);

      String decompressed = huffmanTree.decompress(compressed);
      System.out.println("Decompressed: " + decompressed);
   }
}