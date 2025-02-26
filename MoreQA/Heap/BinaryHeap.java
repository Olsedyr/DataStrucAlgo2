package MoreQA.Heap;

public class BinaryHeap<AnyType extends Comparable<? super AnyType>> {

    /**
     * Construct the binary heap with default capacity.
     */
    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Construct the binary heap with a specified capacity.
     * @param capacity the capacity of the binary heap.
     */
    public BinaryHeap(int capacity) {
        currentSize = 0;
        array = (AnyType[]) new Comparable[capacity + 1]; // +1 because index 0 is unused
    }

    /**
     * Construct the binary heap from an array of items.
     * @param items the array of items to build the heap from.
     */
    public BinaryHeap(AnyType[] items) {
        currentSize = items.length;
        array = (AnyType[]) new Comparable[(currentSize + 2) * 11 / 10]; // Slightly larger array

        int i = 1;
        for (AnyType item : items) {
            array[i++] = item;
        }
        buildHeap(); // Establish heap order
    }

    /**
     * Insert an item into the heap, maintaining heap order.
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        if (currentSize == array.length - 1) {
            enlargeArray(array.length * 2 + 1); // Double the array size if full
        }

        // Percolate up
        int hole = ++currentSize;
        for (array[0] = x; x.compareTo(array[hole / 2]) < 0; hole /= 2) {
            array[hole] = array[hole / 2];
        }
        array[hole] = x;
    }

    /**
     * Enlarge the heap array to a new size.
     * @param newSize the new size of the array.
     */
    private void enlargeArray(int newSize) {
        AnyType[] old = array;
        array = (AnyType[]) new Comparable[newSize];
        for (int i = 0; i < old.length; i++) {
            array[i] = old[i];
        }
    }

    /**
     * Find the smallest item in the heap.
     * @return the smallest item.
     * @throws RuntimeException if the heap is empty.
     */
    public AnyType findMin() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }
        return array[1]; // The smallest item is at the root
    }

    /**
     * Remove and return the smallest item from the heap.
     * @return the smallest item.
     * @throws RuntimeException if the heap is empty.
     */
    public AnyType deleteMin() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty");
        }

        AnyType minItem = findMin();
        array[1] = array[currentSize--]; // Move the last item to the root
        percolateDown(1); // Restore heap order
        return minItem;
    }

    /**
     * Establish heap order property from an arbitrary arrangement of items.
     * Runs in linear time.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * Check if the heap is empty.
     * @return true if the heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Make the heap logically empty.
     */
    public void makeEmpty() {
        currentSize = 0;
    }

    /**
     * Internal method to percolate down in the heap.
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {
        int child;
        AnyType tmp = array[hole]; // Save the item to be percolated down

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2; // Left child
            if (child != currentSize && array[child + 1].compareTo(array[child]) < 0) {
                child++; // Right child is smaller
            }
            if (array[child].compareTo(tmp) < 0) {
                array[hole] = array[child]; // Move smaller child up
            } else {
                break;
            }
        }
        array[hole] = tmp; // Place the item in its correct position
    }

    // Constants
    private static final int DEFAULT_CAPACITY = 10;

    // Fields
    private int currentSize; // Number of elements in the heap
    private AnyType[] array; // The heap array

    // Test program
    public static void main(String[] args) {
        int numItems = 10000;
        BinaryHeap<Integer> heap = new BinaryHeap<>();

        // Insert elements into the heap
        for (int i = 37; i != 0; i = (i + 37) % numItems) {
            heap.insert(i);
        }

        // Delete elements from the heap and verify order
        for (int i = 1; i < numItems; i++) {
            int min = heap.deleteMin();
            if (min != i) {
                System.out.println("Oops! Expected " + i + ", got " + min);
            }
        }

        System.out.println("Heap test completed.");
    }
}