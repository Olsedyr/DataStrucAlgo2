import java.util.NoSuchElementException;

public class PriorityQueueChanger {
    private int[] heap;
    private int size;
    private int capacity;
    private boolean isMinHeap;  // true = min-heap, false = max-heap

    public PriorityQueueChanger(int capacity, boolean isMinHeap) {
        this.capacity = capacity;
        this.heap = new int[capacity];
        this.size = 0;
        this.isMinHeap = isMinHeap;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return 2 * i + 1;
    }

    private int rightChild(int i) {
        return 2 * i + 2;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int findIndex(int priority) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == priority) {
                return i;
            }
        }
        throw new NoSuchElementException("Element with priority " + priority + " not found");
    }

    public void insert(int val) {
        if (size == capacity) {
            throw new IllegalStateException("Priority queue is full");
        }
        heap[size] = val;
        size++;
        heapifyUp(size - 1);
    }

    public int extractRoot() {
        if (size == 0) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        int root = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        return root;
    }

    public int peek() {
        if (size == 0) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return heap[0];
    }

    /**
     * Change priority from fromP to toP.
     * Returns true if successful, false if element not found or input invalid.
     */
    public boolean changePriority(int fromP, int toP) {
        if (toP == fromP) {
            System.err.println("New priority must be different from old priority");
            return false;
        }
        int i;
        try {
            i = findIndex(fromP);
        } catch (NoSuchElementException e) {
            System.err.println("Priority " + fromP + " not found in the heap.");
            return false;
        }
        heap[i] = toP;

        if (compare(toP, fromP)) {
            heapifyUp(i);
        } else {
            heapifyDown(i);
        }
        return true;
    }

    // Compare two values based on heap type
    private boolean compare(int a, int b) {
        return isMinHeap ? a < b : a > b;
    }

    private void heapifyUp(int i) {
        while (i > 0 && compare(heap[i], heap[parent(i)])) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void heapifyDown(int i) {
        while (true) {
            int left = leftChild(i);
            int right = rightChild(i);
            int priorityIndex = i;

            if (left < size && compare(heap[left], heap[priorityIndex])) {
                priorityIndex = left;
            }

            if (right < size && compare(heap[right], heap[priorityIndex])) {
                priorityIndex = right;
            }

            if (priorityIndex != i) {
                swap(i, priorityIndex);
                i = priorityIndex;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    public void printHeap() {
        System.out.print((isMinHeap ? "MinHeap" : "MaxHeap") + " heap: ");
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    // Eksempel på brug med error handling
    public static void main(String[] args) {
        System.out.println("Min-Heap eksempel:");
        PriorityQueueChanger minPQ = new PriorityQueueChanger(10, true);
        minPQ.insert(20);
        minPQ.insert(15);
        minPQ.insert(30);
        minPQ.insert(40);
        minPQ.insert(10);
        minPQ.insert(70);
        minPQ.insert(60);
        minPQ.insert(80);
        minPQ.insert(90);
        minPQ.insert(100);
        minPQ.printHeap();

        System.out.println("Peek: " + minPQ.peek());
        System.out.println("Extract min: " + minPQ.extractRoot());
        minPQ.printHeap();

        minPQ.changePriority(30, 5);
        minPQ.printHeap();

        minPQ.changePriority(40, 50);
        minPQ.printHeap();

        System.out.println("\nMax-Heap eksempel:");
        PriorityQueueChanger maxPQ = new PriorityQueueChanger(10, false);
        maxPQ.insert(20);
        maxPQ.insert(15);
        maxPQ.insert(30);
        maxPQ.insert(40);
        maxPQ.insert(10);
        maxPQ.insert(70);
        maxPQ.insert(60);
        maxPQ.insert(80);
        maxPQ.insert(90);
        maxPQ.insert(100);
        maxPQ.printHeap();

        System.out.println("Peek: " + maxPQ.peek());
        System.out.println("Extract max: " + maxPQ.extractRoot());
        maxPQ.printHeap();

        // Ændring af eksisterende element - succes
        if (!maxPQ.changePriority(15, 50)) {
            System.out.println("Could not change priority: element not found or invalid input.");
        }
        maxPQ.printHeap();

        // Forsøg på at ændre prioritet på et element som ikke findes (tidligere fjernet 40)
        if (!maxPQ.changePriority(40, 5)) {
            System.out.println("Could not change priority: element not found or invalid input.");
        }
        maxPQ.printHeap();
    }
}
