package MoreQA.LinkedList;

public class MergeSortedLinkedLists {

    public static void main(String[] args) {
        LinkedList list1 = new LinkedList();
        LinkedList list2 = new LinkedList();

        // Creating first sorted linked list: 1 -> 3 -> 5 -> 7
        list1.append(1);
        list1.append(3);
        list1.append(5);
        list1.append(7);

        // Creating second sorted linked list: 2 -> 4 -> 6 -> 8
        list2.append(2);
        list2.append(4);
        list2.append(6);
        list2.append(8);

        // Print both lists before merging
        System.out.println("List 1: ");
        list1.printList();

        System.out.println("List 2: ");
        list2.printList();

        // Merging the two sorted lists
        LinkedList mergedList = new LinkedList();
        mergedList.head = mergeSortedLists(list1.head, list2.head);

        System.out.println("Merged List: ");
        mergedList.printList();
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

    // Approach 1: Merge two sorted linked lists iteratively
    // Time Complexity: O(m + n), where m and n are the lengths of the two lists
    // Space Complexity: O(1), as we are modifying the nodes in-place
    public static Node mergeSortedLists(Node head1, Node head2) {
        // Create a dummy node to simplify the merge logic
        Node dummy = new Node(-1);
        Node current = dummy;

        while (head1 != null && head2 != null) {
            if (head1.data <= head2.data) {
                current.next = head1;
                head1 = head1.next;
            } else {
                current.next = head2;
                head2 = head2.next;
            }
            current = current.next;
        }

        // Append remaining nodes from either list (if any)
        if (head1 != null) {
            current.next = head1;
        } else {
            current.next = head2;
        }

        return dummy.next;  // Return the merged list starting from the next of dummy
    }
}
