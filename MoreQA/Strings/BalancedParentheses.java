package MoreQA.Strings;

import java.util.*;

public class BalancedParentheses {

    public static void main(String[] args) {
        // Test cases
        String expression1 = "({[]})"; // Balanced
        String expression2 = "([)]";   // Not Balanced
        String expression3 = "((())";  // Not Balanced

        // Testing all approaches
        System.out.println("Approach 1 (Stack) - Expression 1 is balanced: " + isBalancedStack(expression1));
        System.out.println("Approach 2 (Counter) - Expression 1 is balanced: " + isBalancedCounter(expression1));
        System.out.println("Approach 3 (Improved Counter) - Expression 1 is balanced: " + isBalancedImprovedCounter(expression1));
        System.out.println("Approach 1 (Stack) - Expression 2 is balanced: " + isBalancedStack(expression2));
        System.out.println("Approach 2 (Counter) - Expression 2 is balanced: " + isBalancedCounter(expression2));
        System.out.println("Approach 3 (Improved Counter) - Expression 2 is balanced: " + isBalancedImprovedCounter(expression2));
        System.out.println("Approach 1 (Stack) - Expression 3 is balanced: " + isBalancedStack(expression3));
        System.out.println("Approach 2 (Counter) - Expression 3 is balanced: " + isBalancedCounter(expression3));
        System.out.println("Approach 3 (Improved Counter) - Expression 3 is balanced: " + isBalancedImprovedCounter(expression3));
    }

    // Approach 1: Using Stack (Optimal Solution)
    // Time Complexity: O(n) - We traverse each character of the string once, and stack operations (push/pop) are O(1).
    public static boolean isBalancedStack(String expression) {
        Stack<Character> stack = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch); // Push opening parentheses to stack
            } else if (ch == ')' || ch == '}' || ch == ']') {
                if (stack.isEmpty()) {
                    return false; // Closing parentheses without matching opening
                }
                char top = stack.pop();
                if ((ch == ')' && top != '(') || (ch == '}' && top != '{') || (ch == ']' && top != '[')) {
                    return false; // Mismatched parentheses
                }
            }
        }
        return stack.isEmpty(); // If stack is empty, parentheses are balanced
    }

    // Approach 2: Using Counter (Limited to Parentheses)
    // Time Complexity: O(n) - We traverse each character of the string once, and counter operations are O(1).
    public static boolean isBalancedCounter(String expression) {
        int count = 0;

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                count++; // Increase count for opening parenthesis
            } else if (ch == ')') {
                count--; // Decrease count for closing parenthesis
                if (count < 0) {
                    return false; // More closing parentheses than opening
                }
            }
        }

        return count == 0; // If count is zero, parentheses are balanced
    }

    // Approach 3: Improved Counter (Works for Multiple Types of Parentheses)
    // Time Complexity: O(n) - We traverse each character of the string once, and each counter operation is O(1).
    public static boolean isBalancedImprovedCounter(String expression) {
        int round = 0, square = 0, curly = 0;

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                round++; // Track round parentheses
            } else if (ch == ')') {
                round--; // Check for matching round parenthesis
                if (round < 0) {
                    return false; // More closing round parentheses than opening
                }
            } else if (ch == '{') {
                curly++; // Track curly parentheses
            } else if (ch == '}') {
                curly--; // Check for matching curly parenthesis
                if (curly < 0) {
                    return false; // More closing curly parentheses than opening
                }
            } else if (ch == '[') {
                square++; // Track square parentheses
            } else if (ch == ']') {
                square--; // Check for matching square parenthesis
                if (square < 0) {
                    return false; // More closing square parentheses than opening
                }
            }
        }

        return round == 0 && square == 0 && curly == 0; // All types must balance out
    }
}
