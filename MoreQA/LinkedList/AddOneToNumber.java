package MoreQA.LinkedList;

public class AddOneToNumber {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        // Creating a linked list representing the number 9 -> 9 -> 9
        list.append(9);
        list.append(9);
        list.append(9);

        // Print original list
        System.out.println("Original List:");
        list.printList();

        // Add 1 to the number represented by the linked list
        list.head = addOneToLinkedList(list.head);

        // Print the modified list
        System.out.println("List after adding 1:");
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

    // Approach 1: Add one using reverse (simpler approach)
    // Time Complexity: O(n), where n is the number of nodes in the linked list
    // Space Complexity: O(1), as we are modifying the nodes in place
    public static Node addOneToLinkedList(Node head) {
        // Step 1: Reverse the linked list
        head = reverseList(head);

        // Step 2: Add 1 to the reversed linked list
        Node temp = head;
        int carry = 1;

        while (temp != null && carry > 0) {
            int sum = temp.data + carry;
            temp.data = sum % 10;
            carry = sum / 10;
            temp = temp.next;
        }

        // Step 3: If there is any carry left, add a new node
        if (carry > 0) {
            Node newNode = new Node(carry);
            temp.next = newNode;
        }

        // Step 4: Reverse the linked list back to restore the original order
        head = reverseList(head);
        return head;
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
