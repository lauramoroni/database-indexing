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
         if (heap[index].frequency >= heap[parent].frequency) {
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
      int j = 2 * i + 1;

      if (j < size) {

         if (j < size - 1) {
            if (heap[j + 1].frequency > heap[j].frequency) {
               j++;
            }
         }

         if (heap[j].frequency < heap[i].frequency) {
            swap(i, j);
            down1(j);
         }
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
