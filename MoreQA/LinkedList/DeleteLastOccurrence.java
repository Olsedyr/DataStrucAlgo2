public class DeleteLastOccurrence {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        // Creating a linked list: 1 -> 2 -> 3 -> 4 -> 5 -> 3
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);
        list.append(5);
        list.append(3);

        // Print the original list
        System.out.println("Original List:");
        list.printList();

        // Deleting the last occurrence of 3
        int itemToDelete = 3;
        System.out.println("Deleting the last occurrence of " + itemToDelete + ":");
        deleteLastOccurrence(list.head, itemToDelete);

        // Print the modified list
        System.out.println("List after deletion:");
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

    // Approach: Delete last occurrence of an item in the linked list
    // Time Complexity: O(n), where n is the number of nodes in the linked list
    // Space Complexity: O(1), as we are modifying the nodes in place
    public static void deleteLastOccurrence(Node head, int item) {
        if (head == null) {
            return;
        }

        Node temp = head;
        Node lastOccurrence = null;
        Node prevToLastOccurrence = null;

        // Traverse the list and keep track of the last occurrence of the item
        while (temp != null) {
            if (temp.data == item) {
                lastOccurrence = temp;
            }
            temp = temp.next;
        }

        // If the item is found, delete it
        if (lastOccurrence != null) {
            temp = head;
            // If the item to be deleted is the first node
            if (head == lastOccurrence) {
                head = head.next;
                return;
            }

            // Traverse the list again to find the previous node of the last occurrence
            while (temp != null && temp.next != lastOccurrence) {
                temp = temp.next;
            }

            // Delete the last occurrence by updating the previous node's next pointer
            if (temp != null) {
                temp.next = lastOccurrence.next;
            }
        }
    }
}
