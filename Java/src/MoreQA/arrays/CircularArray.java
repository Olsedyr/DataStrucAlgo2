package MoreQA.arrays;

public class CircularArray {
    private int[] buffer;  // Array to store the elements
    private int head;      // Points to the start of the circular array
    private int tail;      // Points to the end of the circular array
    private int size;      // Current number of elements
    private int capacity;  // Maximum size of the circular array

    // Constructor to initialize the circular array with a given capacity
    public CircularArray(int capacity) {
        this.capacity = capacity;
        buffer = new int[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    // Adds an element to the circular array
    public boolean enqueue(int value) {
        if (isFull()) {
            System.out.println("Buffer is full, cannot enqueue.");
            return false;
        }
        buffer[tail] = value;
        tail = (tail + 1) % capacity;  // Move tail to the next position, wrapping around
        size++;
        return true;
    }

    // Removes an element from the circular array
    public int dequeue() {
        if (isEmpty()) {
            System.out.println("Buffer is empty, cannot dequeue.");
            return -1;  // Indicates failure
        }
        int value = buffer[head];
        head = (head + 1) % capacity;  // Move head to the next position, wrapping around
        size--;
        return value;
    }

    // Returns the current number of elements in the circular array
    public int getSize() {
        return size;
    }

    // Checks if the circular array is full
    public boolean isFull() {
        return size == capacity;
    }

    // Checks if the circular array is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Displays the elements in the circular array
    public void display() {
        System.out.print("Circular Array: ");
        for (int i = 0; i < size; i++) {
            System.out.print(buffer[(head + i) % capacity] + " ");
        }
        System.out.println();
    }

    // Main method to test the circular array
    public static void main(String[] args) {
        CircularArray circularArray = new CircularArray(5);

        // Enqueue some elements
        circularArray.enqueue(10);
        circularArray.enqueue(20);
        circularArray.enqueue(30);
        circularArray.enqueue(40);
        circularArray.enqueue(50);
        circularArray.display();  // Expected: 10 20 30 40 50

        // Try to add another element (should not be possible)
        circularArray.enqueue(60);  // Expected: Buffer is full

        // Dequeue elements
        System.out.println("Dequeued: " + circularArray.dequeue());  // Expected: 10
        circularArray.display();  // Expected: 20 30 40 50

        // Add more elements to wrap around
        circularArray.enqueue(60);
        circularArray.enqueue(70);
        circularArray.display();  // Expected: 20 30 40 50 60

        // Dequeue more elements
        System.out.println("Dequeued: " + circularArray.dequeue());  // Expected: 20
        System.out.println("Dequeued: " + circularArray.dequeue());  // Expected: 30
        circularArray.display();  // Expected: 40 50 60
    }
}