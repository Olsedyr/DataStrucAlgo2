public class AdvancedRecursiveStack {

    private int[] stack;
    private int top;

    public AdvancedRecursiveStack(int capacity) {
        stack = new int[capacity];
        top = -1;
    }

    // -------- Normal stack ops --------

    public void push(int value) {
        if (top == stack.length - 1) {
            System.out.println("STACK OVERFLOW");
            return;
        }
        stack[++top] = value;
    }

    public int pop() {
        if (isEmpty()) {
            System.out.println("STACK UNDERFLOW");
            return -1;
        }
        return stack[top--];
    }

    public int peek() {
        if (isEmpty()) return -1;
        return stack[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    // -------- Recursive Print --------

    public void printRecursively() {
        printHelper(top);
        System.out.println();
    }

    private void printHelper(int index) {
        if (index < 0) return;
        System.out.print(stack[index] + " ");
        printHelper(index - 1);
    }

    // -------- Recursive Pop All --------

    public void popAllRecursively() {
        if (isEmpty()) return;
        int val = pop();
        System.out.println("Popped: " + val);
        popAllRecursively();
    }

    // -------- Recursive Push 1..n --------

    public void pushRecursively(int n) {
        if (n == 0) return;
        pushRecursively(n - 1);
        push(n);
    }

    // -------- Reverse Stack Recursively --------

    public void reverse() {
        if (isEmpty()) return;
        int val = pop();
        reverse();
        insertAtBottom(val);
    }

    private void insertAtBottom(int value) {
        if (isEmpty()) {
            push(value);
            return;
        }
        int topVal = pop();
        insertAtBottom(value);
        push(topVal);
    }

    // -------- Sort Stack Recursively --------

    public void sort() {
        if (isEmpty()) return;
        int val = pop();
        sort();
        sortedInsert(val);
    }

    private void sortedInsert(int value) {
        if (isEmpty() || peek() <= value) {
            push(value);
            return;
        }
        int topVal = pop();
        sortedInsert(value);
        push(topVal);
    }

    // -------- Recursive Max Finder --------

    public int findMax() {
        return maxHelper(top);
    }

    private int maxHelper(int index) {
        if (index == 0) return stack[0];
        return Math.max(stack[index], maxHelper(index - 1));
    }

    // -------- Recursive Search --------

    public boolean contains(int target) {
        return searchHelper(top, target);
    }

    private boolean searchHelper(int index, int target) {
        if (index < 0) return false;
        if (stack[index] == target) return true;
        return searchHelper(index - 1, target);
    }

    // -------- Recursive Sum --------

    public int sum() {
        return sumHelper(top);
    }

    private int sumHelper(int index) {
        if (index < 0) return 0;
        return stack[index] + sumHelper(index - 1);
    }


    // -------- MAIN for testing --------

    public static void main(String[] args) {
        AdvancedRecursiveStack s = new AdvancedRecursiveStack(20);

        s.push(3);
        s.push(1);
        s.push(4);
        s.push(2);

        System.out.print("Stack: ");
        s.printRecursively();

        System.out.println("Max: " + s.findMax());
        System.out.println("Sum: " + s.sum());
        System.out.println("Contains 4? " + s.contains(4));

        System.out.println("Sorting...");
        s.sort();
        s.printRecursively();

        System.out.println("Reversing...");
        s.reverse();
        s.printRecursively();
    }
}
