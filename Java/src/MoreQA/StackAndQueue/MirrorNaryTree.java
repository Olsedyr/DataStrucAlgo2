package MoreQA.StackAndQueue;

import java.util.*;

public class MirrorNaryTree {

    static class Node {
        int data;
        List<Node> children;

        // Constructor
        public Node(int data) {
            this.data = data;
            children = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        // Creating first N-ary tree
        Node root1 = new Node(1);
        root1.children.add(new Node(2));
        root1.children.add(new Node(3));
        root1.children.add(new Node(4));

        // Creating second N-ary tree
        Node root2 = new Node(1);
        root2.children.add(new Node(4));
        root2.children.add(new Node(3));
        root2.children.add(new Node(2));

        // Approach 1: Using Recursion
        System.out.println("Approach 1: Using Recursion to check mirror:");
        System.out.println(isMirrorRecursive(root1, root2));

        // Approach 2: Using Iterative Method
        System.out.println("Approach 2: Using Iterative method to check mirror:");
        System.out.println(isMirrorIterative(root1, root2));
    }

    // Approach 1: Using Recursion
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(h) where h is the height of the tree due to recursive function calls.
    public static boolean isMirrorRecursive(Node root1, Node root2) {
        // If both trees are empty
        if (root1 == null && root2 == null) {
            return true;
        }

        // If one tree is empty and other is not
        if (root1 == null || root2 == null) {
            return false;
        }

        // If data of root nodes are different
        if (root1.data != root2.data) {
            return false;
        }

        // If number of children are not same
        if (root1.children.size() != root2.children.size()) {
            return false;
        }

        // Compare children recursively in reverse order
        int n = root1.children.size();
        for (int i = 0; i < n; i++) {
            if (!isMirrorRecursive(root1.children.get(i), root2.children.get(n - i - 1))) {
                return false;
            }
        }

        return true;
    }

    // Approach 2: Using Iterative Method (Using Queue)
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(n) due to queue space used.
    public static boolean isMirrorIterative(Node root1, Node root2) {
        // If both trees are empty
        if (root1 == null && root2 == null) {
            return true;
        }

        // If one tree is empty and other is not
        if (root1 == null || root2 == null) {
            return false;
        }

        // Initialize two queues for both trees
        Queue<Node> queue1 = new LinkedList<>();
        Queue<Node> queue2 = new LinkedList<>();

        // Add root nodes to both queues
        queue1.add(root1);
        queue2.add(root2);

        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            Node node1 = queue1.poll();
            Node node2 = queue2.poll();

            // If data of current nodes are different
            if (node1.data != node2.data) {
                return false;
            }

            // If number of children are not same
            if (node1.children.size() != node2.children.size()) {
                return false;
            }

            // Add children to respective queues, in reverse order for node2
            for (int i = 0; i < node1.children.size(); i++) {
                queue1.add(node1.children.get(i));
                queue2.add(node2.children.get(node2.children.size() - 1 - i));
            }
        }

        // If both queues are empty, trees are mirrors
        return queue1.isEmpty() && queue2.isEmpty();
    }
}
