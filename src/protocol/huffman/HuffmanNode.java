package protocol.huffman;

public class HuffmanNode {
   char character;
   int frequency;
   HuffmanNode left, right;

   public HuffmanNode(char character, int frequency) {
      this.character = character;
      this.frequency = frequency;
   }

   public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
      this.frequency = frequency;
      this.left = left;
      this.right = right;
   }

   public boolean isLeaf() {
      return left == null && right == null;
   }
}
