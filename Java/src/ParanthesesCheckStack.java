public class ParanthesesCheckStack {

    // Stack implementation
    private char[] stackArray;
    private int top;

    // Constructor
    public ParanthesesCheckStack(int size) {
        stackArray = new char[size];
        top = -1;
    }

    public void push(char value) {
        stackArray[++top] = value;
    }

    public char pop() {
        return stackArray[top--];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    /* =========================
       ITERATIV VERSION
       ========================= */
    public static boolean balParIterative(String text) {
        ParanthesesCheckStack stack = new ParanthesesCheckStack(text.length());

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (current == '(' || current == '{' || current == '[') {
                stack.push(current);
            }

            if (current == ')' || current == '}' || current == ']') {
                if (stack.isEmpty()) {
                    return false;
                }

                char lastOpened = stack.pop();
                if (!isMatchingPair(lastOpened, current)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    /* =========================
       REKURSIV VERSION
       ========================= */
    public static boolean balParRecursive(String text) {
        ParanthesesCheckStack stack = new ParanthesesCheckStack(text.length());
        return balParRecursive(text, 0, stack);
    }

    private static boolean balParRecursive(String text, int index, ParanthesesCheckStack stack) {
        // Base case: end of string
        if (index == text.length()) {
            return stack.isEmpty();
        }

        char current = text.charAt(index);

        if (current == '(' || current == '{' || current == '[') {
            stack.push(current);
        }

        if (current == ')' || current == '}' || current == ']') {
            if (stack.isEmpty()) {
                return false;
            }

            char lastOpened = stack.pop();
            if (!isMatchingPair(lastOpened, current)) {
                return false;
            }
        }

        // Recursive call
        return balParRecursive(text, index + 1, stack);
    }

    // Helper method
    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
                (open == '{' && close == '}') ||
                (open == '[' && close == ']');
    }

    public static void main(String[] args) {
        String test = "({[])}";

        System.out.println("Iterative: " + balParIterative(test));
        System.out.println("Recursive: " + balParRecursive(test));
    }
}
