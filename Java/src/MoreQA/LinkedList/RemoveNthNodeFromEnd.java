package MoreQA.LinkedList;

public class RemoveNthNodeFromEnd {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        // Creating a linked list: 1 -> 2 -> 3 -> 4 -> 5
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);
        list.append(5);

        // Print original list
        System.out.println("Original List:");
        list.printList();

        // Remove the 2nd node from the end
        int n = 2;
        list.head = removeNthFromEnd(list.head, n);

        // Print the modified list
        System.out.println("List after removing the " + n + "th node from the end:");
        list.printList();
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

    // Approach 1: Using two pointers (Fast and Slow)
    // Time Complexity: O(n), where n is the number of nodes in the linked list
    // Space Complexity: O(1), as we are using only two pointers
    public static Node removeNthFromEnd(Node head, int n) {
        // Create a dummy node to simplify the case when the head is removed
        Node dummy = new Node(0);
        dummy.next = head;
        Node fast = dummy;
        Node slow = dummy;

        // Move the fast pointer n+1 steps ahead to create a gap
        for (int i = 0; i <= n; i++) {
            if (fast == null) {
                return null; // If n is greater than the length of the list
            }
            fast = fast.next;
        }

        // Move both fast and slow pointers until fast reaches the end
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }

        // Now slow points to the node before the one we want to remove
        slow.next = slow.next.next; // Remove the nth node from the end

        return dummy.next; // Return the new head (dummy.next)
    }
}
