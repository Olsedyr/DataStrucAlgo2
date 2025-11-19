public class StackImplementation {


    //Implement STack
    private char[] stackArray;
    private int top;




    // Constructor to initialize the stack with a given size
    public StackImplementation(int size) {
        stackArray = new char[size];
        top = -1;
    }

    // Method to push a character onto the stack
    public void push(char value) {
        stackArray[++top] = value;
    }

    // Method to pop a character from the stack
    public char pop() {
        return stackArray[top--];
    }

    // Method to check if the stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Method to check if the parentheses in the text are balanced
    public static boolean balPar(String text) {
        StackImplementation stack = new StackImplementation(text.length());

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            // Push opening brackets into the stack
            if (current == '(' || current == '{' || current == '[') {
                stack.push(current);
            }

            // When encountering a closing bracket
            if (current == ')' || current == '}' || current == ']') {
                if (stack.isEmpty()) {
                    return false;  // No matching opening bracket
                }

                char lastOpened = stack.pop();
                if (!isMatchingPair(lastOpened, current)) {
                    return false;  // Mismatched brackets
                }
            }
        }



        // If the stack is not empty, it means there are unmatched opening brackets
        return stack.isEmpty();
    }

    // Helper method to check if the brackets match
    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') ||
                (open == '{' && close == '}') ||
                (open == '[' && close == ']');
    }

    public static void main(String[] args) {
        // Test cases
        System.out.println(balPar("()"));        // true
        System.out.println(balPar("(())"));      // true
        System.out.println(balPar("(()"));       // false
        System.out.println(balPar("({[]})"));    // true
        System.out.println(balPar("{[(])}"));    // false
    }
}