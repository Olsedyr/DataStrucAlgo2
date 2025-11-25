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

        // Merging the two sorted lists recursively
        LinkedList mergedList = new LinkedList();
        mergedList.head = mergeSortedListsRecursive(list1.head, list2.head);

        System.out.println("Merged List (Recursive): ");
        mergedList.printList();

        // Optional: merge using iterative method
        /*
        LinkedList mergedListIter = new LinkedList();
        mergedListIter.head = mergeSortedLists(list1.head, list2.head);
        System.out.println("Merged List (Iterative): ");
        mergedListIter.printList();
        */
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

    // Iterative merge of two sorted linked lists
    public static Node mergeSortedLists(Node head1, Node head2) {
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

        if (head1 != null) {
            current.next = head1;
        } else {
            current.next = head2;
        }

        return dummy.next;
    }

    // Recursive merge of two sorted linked lists
    public static Node mergeSortedListsRecursive(Node head1, Node head2) {
        if (head1 == null) return head2;
        if (head2 == null) return head1;

        if (head1.data <= head2.data) {
            head1.next = mergeSortedListsRecursive(head1.next, head2);
            return head1;
        } else {
            head2.next = mergeSortedListsRecursive(head1, head2.next);
            return head2;
        }
    }
}
