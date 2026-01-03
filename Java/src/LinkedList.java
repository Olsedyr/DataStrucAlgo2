public class LinkedList<I extends Number> {

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

    public LinkedList() {
        head = null;
    }

    // Public method to add an element to the end (rekursivt)
    public void add(int data) {
        head = addRecursive(head, data);
    }

    // Rekursiv hjælpefunktion til at tilføje element
    private Node addRecursive(Node current, int data) {
        if (current == null) {
            return new Node(data);  // base case: tom plads, lav ny node
        }
        current.next = addRecursive(current.next, data); // gå videre til næste
        return current;
    }

    // Public method to display list rekursivt
    public void display() {
        if (head == null) {
            System.out.println("The list is empty.");
        } else {
            displayRecursive(head);
            System.out.println();
        }
    }

    private void displayRecursive(Node node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        displayRecursive(node.next);
    }

    // Public method to remove element rekursivt
    public void remove(int data) {
        head = removeRecursive(head, data);
    }

    // Rekursiv hjælpefunktion til at fjerne element
    private Node removeRecursive(Node current, int data) {
        if (current == null) {
            System.out.println("Element not found in the list.");
            return null;
        }

        if (current.data == data) {
            // Found element — fjern ved at returnere næste node (spring current over)
            return current.next;
        }

        current.next = removeRecursive(current.next, data);
        return current;
    }

    public static void main(String[] args) {
        LinkedList<Number> list = new LinkedList<Number>();

        list.add(10);
        list.add(20);
        list.add(30);

        System.out.print("List after adding elements: ");
        list.display();  // 10 20 30

        list.remove(20);
        System.out.print("List after removing 20: ");
        list.display();  // 10 30

        list.remove(40);  // Element ikke i listen - printer "Element not found in the list."
    }
}
