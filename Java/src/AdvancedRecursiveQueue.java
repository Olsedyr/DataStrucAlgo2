
public class AdvancedRecursiveQueue {

    private int[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public AdvancedRecursiveQueue(int capacity) {
        this.capacity = capacity;
        queue = new int[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    // -------- Normal queue ops --------

    public void enqueue(int value) {
        if (isFull()) {
            System.out.println("QUEUE FULL");
            return;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = value;
        size++;
    }

    public int dequeue() {
        if (isEmpty()) {
            System.out.println("QUEUE EMPTY");
            return -1;
        }
        int val = queue[front];
        front = (front + 1) % capacity;
        size--;
        return val;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // -------- Recursive Print --------

    public void printRecursively() {
        printHelper(0);
        System.out.println();
    }

    private void printHelper(int index) {
        if (index == size) return;
        int real = (front + index) % capacity;
        System.out.print(queue[real] + " ");
        printHelper(index + 1);
    }

    // -------- Dequeue All Recursively --------

    public void dequeueAllRecursively() {
        if (isEmpty()) return;
        int val = dequeue();
        System.out.println("Dequeued: " + val);
        dequeueAllRecursively();
    }

    // -------- Reverse Queue Recursively --------

    public void reverse() {
        if (isEmpty()) return;
        int val = dequeue();
        reverse();
        enqueue(val);
    }

    // -------- Recursive Search --------

    public boolean contains(int target) {
        return searchHelper(0, target);
    }

    private boolean searchHelper(int index, int target) {
        if (index == size) return false;
        int real = (front + index) % capacity;
        if (queue[real] == target) return true;
        return searchHelper(index + 1, target);
    }

    // -------- Recursive Sum --------

    public int sum() {
        return sumHelper(0);
    }

    private int sumHelper(int index) {
        if (index == size) return 0;
        int real = (front + index) % capacity;
        return queue[real] + sumHelper(index + 1);
    }

    // -------- Recursive Max --------

    public int findMax() {
        if (isEmpty()) return -1;
        return maxHelper(0);
    }

    private int maxHelper(int index) {
        if (index == size - 1) {
            return queue[(front + index) % capacity];
        }
        int real = (front + index) % capacity;
        return Math.max(real, maxHelper(index + 1));
    }


    // -------- MAIN for testing --------

    public static void main(String[] args) {
        AdvancedRecursiveQueue q = new AdvancedRecursiveQueue(20);

        q.enqueue(5);
        q.enqueue(1);
        q.enqueue(9);
        q.enqueue(3);

        q.printRecursively();

        System.out.println("Max: " + q.findMax());
        System.out.println("Sum: " + q.sum());
        System.out.println("Contains 3? " + q.contains(3));

        System.out.println("Reversing...");
        q.reverse();
        q.printRecursively();

        System.out.println("Dequeue all...");
        q.dequeueAllRecursively();
    }
}
