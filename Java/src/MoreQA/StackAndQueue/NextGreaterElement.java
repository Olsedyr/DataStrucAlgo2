package MoreQA.StackAndQueue;

import java.util.Stack;
import java.util.HashMap;

public class NextGreaterElement {

    public static void main(String[] args) {
        int[] arr = {4, 5, 2, 10, 8};

        // Approach 1: Using Stack
        System.out.println("Next greater elements using Stack:");
        printNextGreaterUsingStack(arr);

        // Approach 2: Using HashMap for optimized space (no Stack)
        System.out.println("\nNext greater elements using HashMap:");
        printNextGreaterUsingHashMap(arr);
    }

    // Approach 1: Using Stack to find the next greater element for each element
    // Time Complexity: O(n), where n is the number of elements in the array.
    // Space Complexity: O(n), due to the stack.
    public static void printNextGreaterUsingStack(int[] arr) {
        Stack<Integer> stack = new Stack<>();

        // Traverse the array from right to left
        for (int i = arr.length - 1; i >= 0; i--) {
            // While stack is not empty and the top element is less than or equal to arr[i], pop from stack
            while (!stack.isEmpty() && stack.peek() <= arr[i]) {
                stack.pop();
            }

            // If stack is empty, there is no greater element for arr[i], else the top element is the next greater element
            if (stack.isEmpty()) {
                System.out.println(arr[i] + " --> -1");
            } else {
                System.out.println(arr[i] + " --> " + stack.peek());
            }

            // Push the current element to the stack
            stack.push(arr[i]);
        }
    }

    // Approach 2: Using HashMap to store next greater elements for each element in the array
    // Time Complexity: O(n), where n is the number of elements in the array.
    // Space Complexity: O(n), due to the HashMap.
    public static void printNextGreaterUsingHashMap(int[] arr) {
        int n = arr.length;
        HashMap<Integer, Integer> nextGreaterMap = new HashMap<>();
        Stack<Integer> stack = new Stack<>();

        // Traverse the array from left to right
        for (int i = 0; i < n; i++) {
            // While stack is not empty and the top element of the stack is less than arr[i],
            // we pop the stack and store the next greater element in the map
            while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
                int index = stack.pop();
                nextGreaterMap.put(arr[index], arr[i]);
            }

            // Push the current element's index to the stack
            stack.push(i);
        }

        // Print the next greater elements from the map
        for (int i = 0; i < n; i++) {
            if (nextGreaterMap.containsKey(arr[i])) {
                System.out.println(arr[i] + " --> " + nextGreaterMap.get(arr[i]));
            } else {
                System.out.println(arr[i] + " --> -1");
            }
        }
    }
}
