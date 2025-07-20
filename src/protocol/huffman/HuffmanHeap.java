package protocol.huffman;

// min heap for Huffman nodes 
public class HuffmanHeap {
   HuffmanNode[] heap;
   int size;

   public HuffmanHeap(int capacity) {
      heap = new HuffmanNode[capacity];
      size = 0;
   }

   public int getSize() {
      return size;
   }

   public void insert(HuffmanNode node) {
      if (isFull()) {
         throw new IllegalStateException("Heap is full");
      }

      heap[size] = node;
      up(size);
      size++;
   }

   public void up(int index) {
      while (index > 0) {
         int parent = (index - 1) / 2;
         if (heap[index].frequency <= heap[parent].frequency) { // menor frequencia sobe
            swap(index, parent);
            index = parent;
         } else {
            break;
         }
      }
   }

   public HuffmanNode removeMin() {
      if (isEmpty()) {
         throw new IllegalStateException("Heap is empty");
      }

      HuffmanNode min = heap[0];
      heap[0] = heap[--size];
      down1(0);

      return min;
   }

   private void down1(int i) {
      int smallestChild;
      while (true) {
         int leftChild = 2 * i + 1;
         int rightChild = 2 * i + 2;

         if (leftChild >= size) {
            break;
         }

         // índice do menor filho
         smallestChild = leftChild;
         if (rightChild < size && heap[rightChild].frequency < heap[leftChild].frequency) {
            smallestChild = rightChild;
         }

         // se o pai for menor ou igual ao menor dos filhos, a propriedade do heap está correta
         if (heap[i].frequency <= heap[smallestChild].frequency) {
            break;
         }

         //  troca o pai com o menor filho e continua a descida
         swap(i, smallestChild);
         i = smallestChild;
      }
   }

   private void swap(int i, int j) {
      HuffmanNode temp = heap[i];
      heap[i] = heap[j];
      heap[j] = temp;
   }

   private boolean isEmpty() {
      return size == 0;
   }

   private boolean isFull() {
      return size == heap.length;
   }

   public void buildHeap(HuffmanNode[] nodes) {
      size = nodes.length;
      heap = nodes.clone();

      for (int i = (size - 2) / 2; i >= 0; i--) {
         down1(i);
      }
   }

   public void buildHeap() {
      for (int i = (size / 2) - 1; i >= 0; i--) {
         down1(i);
      }
   }


}
