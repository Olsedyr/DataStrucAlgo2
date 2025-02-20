package MoreQA.LinkedList;

public class ReverseLinkedList {

    public static void main(String[] args) {
        LinkedList list1 = new LinkedList();
        list1.append(1);
        list1.append(2);
        list1.append(3);
        list1.append(4);
        list1.append(5);

        System.out.println("Original List:");
        list1.printList();

        // Testing all approaches
        list1.reverseIterative(); // Iterative approach
        System.out.println("Reversed List (Iterative):");
        list1.printList();

        // Reset the list and reverse recursively
        list1 = new LinkedList();
        list1.append(1);
        list1.append(2);
        list1.append(3);
        list1.append(4);
        list1.append(5);

        list1.reverseRecursive(list1.head); // Recursive approach
        System.out.println("Reversed List (Recursive):");
        list1.printList();
    }

    // Linked list Node
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    // Linked list class
    static class LinkedList {
        Node head;

        // Function to append a new node to the linked list
        public void append(int new_data) {
            Node new_node = new Node(new_data);
            if (head == null) {
                head = new_node;
                return;
            }
            Node last = head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = new_node;
        }

        // Function to print the linked list
        public void printList() {
            Node temp = head;
            while (temp != null) {
                System.out.print(temp.data + " ");
                temp = temp.next;
            }
            System.out.println();
        }

        // Approach 1: Iterative approach to reverse the linked list
        // Time Complexity: O(n) where n is the number of nodes in the linked list
        public void reverseIterative() {
            Node prev = null;
            Node current = head;
            Node next = null;

            while (current != null) {
                next = current.next;  // Save next node
                current.next = prev;  // Reverse current node's pointer
                prev = current;       // Move pointers one step forward
                current = next;
            }
            head = prev;  // Update head to the last node
        }

        // Approach 2: Recursive approach to reverse the linked list
        // Time Complexity: O(n) where n is the number of nodes in the linked list
        public Node reverseRecursive(Node node) {
            // Base case: if head is null or there is only one node
            if (node == null || node.next == null) {
                return node;
            }

            // Recursively reverse the rest of the list
            Node rest = reverseRecursive(node.next);

            // Set the next node's next pointer to the current node
            node.next.next = node;
            node.next = null;  // Set current node's next to null

            return rest;  // Return the new head of the reversed list
        }
    }
}
