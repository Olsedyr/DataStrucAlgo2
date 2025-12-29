package MoreQA.arrays;

public class generic_circular_array<T> {
    private T[] buffer;  // Array til at gemme elementerne
    private int head;      // Startposition i cirklen
    private int tail;      // Slutposition i cirklen
    private int size;      // Antal elementer i cirklen
    private final int capacity;  // Maks størrelse

    // Constructor
    @SuppressWarnings("unchecked")
    public generic_circular_array(int capacity) {
        this.capacity = capacity;
        // Java tillader ikke direkte oprettelse af generiske arrays, så vi bruger Object[] og caster
        this.buffer = (T[]) new Object[capacity];
        head = 0;
        tail = 0;
        size = 0;
    }

    public boolean enqueue(T value) {
        if (isFull()) {
            System.out.println("Buffer is full, cannot enqueue.");
            return false;
        }
        buffer[tail] = value;
        tail = (tail + 1) % capacity;
        size++;
        return true;
    }


    //Forklaring:
    //
    //index kan være enten head eller tail.
    //
    //Når du flytter head eller tail frem med 1, skal det ikke overskride arrayets størrelse (capacity).
    //
    //% capacity (modulo) sikrer, at hvis index + 1 når op på capacity, så nulstilles det til 0 — altså starter forfra i starten af arrayet.
    public T dequeue() {
        if (isEmpty()) {
            System.out.println("Buffer is empty, cannot dequeue.");
            return null;  // Returnerer null ved fejl (alternativt kan du kaste exception)
        }
        T value = buffer[head];
        buffer[head] = null; // Hjælper Garbage Collector
        head = (head + 1) % capacity;
        size--;
        return value;
    }

    public int getSize() {
        return size;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void display() {
        System.out.print("Circular Array: ");
        for (int i = 0; i < size; i++) {
            System.out.print(buffer[(head + i) % capacity] + " ");
        }
        System.out.println();
    }

    // Test af den generiske CircularArray
    public static void main(String[] args) {
        generic_circular_array circularArray = new generic_circular_array(5);

        circularArray.enqueue(10);
        circularArray.enqueue(50);
        circularArray.enqueue(80);
        circularArray.enqueue(70);
        circularArray.enqueue(90);
        circularArray.display();  // Forventet: A B C D E

        circularArray.enqueue("F");  // Buffer is full

        System.out.println("Dequeued: " + circularArray.dequeue());  // A
        circularArray.display();  // Forventet: B C D E

        circularArray.enqueue(82);
        circularArray.enqueue(110);
        circularArray.display();  // Forventet: B C D E F

        System.out.println("Dequeued: " + circularArray.dequeue());  // B
        System.out.println("Dequeued: " + circularArray.dequeue());  // C
        circularArray.display();  // Forventet: D E F
    }
}
