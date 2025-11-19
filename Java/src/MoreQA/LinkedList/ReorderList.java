package MoreQA.LinkedList;

public class ReorderList {

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

        // Reorder the linked list
        reorderList(list.head);

        // Print the reordered list
        System.out.println("Reordered List:");
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

    // Approach 1: Reordering the list using a two-pointer approach
    // Time Complexity: O(n), where n is the number of nodes in the linked list
    // Space Complexity: O(1), as we are reordering in-place
    public static void reorderList(Node head) {
        if (head == null || head.next == null) {
            return; // No need to reorder if the list is empty or has only one node
        }

        // Step 1: Find the middle of the linked list using the slow and fast pointer method
        Node slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Step 2: Split the list into two halves
        Node secondHalf = slow.next;
        slow.next = null; // End the first half of the list

        // Step 3: Reverse the second half of the list
        secondHalf = reverseList(secondHalf);

        // Step 4: Merge the two halves alternatively
        Node firstHalf = head;
        while (firstHalf != null && secondHalf != null) {
            Node temp1 = firstHalf.next;
            Node temp2 = secondHalf.next;

            firstHalf.next = secondHalf;
            secondHalf.next = temp1;

            firstHalf = temp1;
            secondHalf = temp2;
        }
    }

    // Helper function to reverse a linked list
    // Time Complexity: O(n), where n is the number of nodes in the linked list
    // Space Complexity: O(1), as we are reversing in-place
    public static Node reverseList(Node head) {
        Node prev = null, curr = head, next = null;
        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }
}
