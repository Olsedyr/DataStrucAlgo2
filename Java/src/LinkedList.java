public class LinkedList {

    // Node class to represent each element in the linked list
    private class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;  // Head of the linked list

    // Constructor to initialize an empty list
    public LinkedList() {
        head = null;
    }

    // Method to add a new element to the end of the list
    public void add(int data) {
        Node newNode = new Node(data);

        if (head == null) {  // If the list is empty, make the new node the head
            head = newNode;
        } else {
            Node current = head;
            // Traverse to the last node
            while (current.next != null) {
                current = current.next;
            }
            // Set the new node at the end
            current.next = newNode;
        }
    }

    // Method to display all elements in the list
    public void display() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    // Method to remove an element from the list
    public void remove(int data) {
        if (head == null) {  // If the list is empty
            System.out.println("List is empty, nothing to remove.");
            return;
        }

        // If the element to remove is the head
        if (head.data == data) {
            head = head.next;  // Move head to the next node
            return;
        }

        // Otherwise, search for the element
        Node current = head;
        while (current.next != null && current.next.data != data) {
            current = current.next;
        }

        // If the element is found, unlink it
        if (current.next != null) {
            current.next = current.next.next;
        } else {
            System.out.println("Element not found in the list.");
        }
    }

    // Main method to test the linked list
    public static void main(String[] args) {
       LinkedList list = new LinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        System.out.print("List after adding elements: ");
        list.display();  // Expected output: 10 20 30

        list.remove(20);
        System.out.print("List after removing 20: ");
        list.display();  // Expected output: 10 30

        list.remove(40);  // Element not in the list
    }
}

