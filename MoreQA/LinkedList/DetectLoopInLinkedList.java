package MoreQA.LinkedList;

public class DetectLoopInLinkedList {

    public static void main(String[] args) {
        LinkedList list1 = new LinkedList();
        list1.append(1);
        list1.append(2);
        list1.append(3);
        list1.append(4);
        list1.append(5);

        // Create a loop for testing
        list1.head.next.next.next.next.next = list1.head.next.next; // Creating a loop (5 -> 3)

        // Testing all approaches
        System.out.println("Approach 1 (Floyd's Cycle-Finding Algorithm): " + list1.hasLoopFloyd());
        System.out.println("Approach 2 (Hashing): " + list1.hasLoopHashing());
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

        // Approach 1: Floyd's Cycle-Finding Algorithm (Tortoise and Hare)
        // Time Complexity: O(n), where n is the number of nodes in the linked list
        // Space Complexity: O(1), as we are using only two pointers
        public boolean hasLoopFloyd() {
            if (head == null) return false;

            Node slow = head;
            Node fast = head;

            // Move slow pointer by 1 and fast pointer by 2 steps
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;

                if (slow == fast) { // If they meet, there's a loop
                    return true;
                }
            }

            return false; // No loop detected
        }

        // Approach 2: Using Hashing
        // Time Complexity: O(n), where n is the number of nodes in the linked list
        // Space Complexity: O(n), as we are storing the nodes in a HashSet
        public boolean hasLoopHashing() {
            if (head == null) return false;

            Node current = head;
            java.util.HashSet<Node> nodesSet = new java.util.HashSet<>();

            while (current != null) {
                if (nodesSet.contains(current)) { // If node is already in the set, a loop is detected
                    return true;
                }
                nodesSet.add(current);
                current = current.next;
            }

            return false; // No loop detected
        }
    }
}
