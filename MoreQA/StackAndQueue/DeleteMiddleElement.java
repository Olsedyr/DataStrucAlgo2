package MoreQA.StackAndQueue;

import java.util.Stack;

public class DeleteMiddleElement {

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        System.out.println("Original Stack: " + stack);

        // Approach 1: Using Recursion
        System.out.println("\nDeleting middle element using recursion:");
        deleteMiddleUsingRecursion(stack, stack.size());

        // Reset the stack for next approach
        stack.clear();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        // Approach 2: Using Stack Index
        System.out.println("\nDeleting middle element using stack index:");
        deleteMiddleUsingStackIndex(stack);
    }

    // Approach 1: Using Recursion to delete the middle element
    // Time Complexity: O(n), where n is the number of elements in the stack.
    // Space Complexity: O(n) due to recursive function calls.
    public static void deleteMiddleUsingRecursion(Stack<Integer> stack, int size) {
        // Base case: If stack is empty or only one element is left
        if (stack.isEmpty() || size == 0) {
            return;
        }

        // Recursively pop elements and reach the middle element
        int popped = stack.pop();
        deleteMiddleUsingRecursion(stack, size - 1);

        // Once the middle element is reached, we do not push it back
        if (size != (stack.size() + 1)) {
            stack.push(popped);
        }

        // Print the modified stack
        if (stack.size() == 1) {
            System.out.println(stack);
        }
    }

    // Approach 2: Using Stack Index to delete the middle element
    // Time Complexity: O(n), where n is the number of elements in the stack.
    // Space Complexity: O(n) due to the recursive call stack or the extra stack used.
    public static void deleteMiddleUsingStackIndex(Stack<Integer> stack) {
        int size = stack.size();
        int mid = size / 2;

        // Remove elements until reaching the middle element
        for (int i = 0; i < mid; i++) {
            stack.push(stack.pop());
        }

        // Remove the middle element
        stack.pop();

        // Restore the elements after the middle element
        for (int i = mid + 1; i < size; i++) {
            stack.push(stack.pop());
        }

        // Print the modified stack
        System.out.println(stack);
    }
}
