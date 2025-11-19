package MoreQA.LinkedList;

public class MiddleOfLinkedList {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        // Creating a linked list: 1 -> 2 -> 3 -> 4 -> 5 -> 6
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);
        list.append(5);
        list.append(6);

        // Print the original list
        System.out.println("Original List:");
        list.printList();

        // Approach 1: Print the middle element using the slow and fast pointer approach
        System.out.println("Middle element (using slow and fast pointers):");
        printMiddleUsingSlowFast(list.head);
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
    }

    // Approach 1: Using the slow and fast pointer technique to find the middle element
    // Time Complexity: O(n), where n is the number of nodes in the linked list
    // Space Complexity: O(1), as we are using only a constant amount of extra space
    public static void printMiddleUsingSlowFast(Node head) {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        Node slow = head;
        Node fast = head;

        // Move fast pointer by 2 steps and slow pointer by 1 step
        // When fast pointer reaches the end, slow pointer will be at the middle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // At the end of this loop, slow pointer will be at the middle
        System.out.println("Middle element is: " + slow.data);
    }
}
