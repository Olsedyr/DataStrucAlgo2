public class LinkedListSumCheck {

    // Singly linked list node
    static class SinglyNode {
        int value;
        SinglyNode next;
        SinglyNode(int value) { this.value = value; }
    }

    // Singly linked list
    static class SinglyLinkedList {
        SinglyNode head;

        public void add(int value) {
            SinglyNode newNode = new SinglyNode(value);
            if (head == null) {
                head = newNode;
                return;
            }
            SinglyNode curr = head;
            while (curr.next != null) curr = curr.next;
            curr.next = newNode;
        }

        public void print() {
            SinglyNode curr = head;
            while (curr != null) {
                System.out.print(curr.value + " -> ");
                curr = curr.next;
            }
            System.out.println("null");
        }
    }

    // Doubly linked list node
    static class DoublyNode {
        int value;
        DoublyNode next, prev;
        DoublyNode(int value) { this.value = value; }
    }

    // Doubly linked list
    static class DoublyLinkedList {
        DoublyNode head, tail;

        public void add(int value) {
            DoublyNode newNode = new DoublyNode(value);
            if (head == null) {
                head = tail = newNode;
                return;
            }
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        public void print() {
            DoublyNode curr = head;
            while (curr != null) {
                System.out.print(curr.value + " <-> ");
                curr = curr.next;
            }
            System.out.println("null");
        }
    }

    // Global variabel for singly tjek
    static boolean allValidSingly;

    // Rekursiv check singly linked list, print kun gyldige tripletter, sæt allValidSingly false ved fejl
    static void checkSumProductSingly(SinglyNode node, int i) {
        if (node == null || node.next == null || node.next.next == null) return;

        int x = node.value;
        int y = node.next.value;
        int z = node.next.next.value;

        boolean valid = false;

        if (x + y == z) {
            System.out.printf("Element [%d] plus element [%d] giver samme som element [%d]%n", i, i + 1, i + 2);
            valid = true;
        }
        if (x * y == z) {
            System.out.printf("Element [%d] gange element [%d] giver samme som element [%d]%n", i, i + 1, i + 2);
            valid = true;
        }

        if (!valid) allValidSingly = false;

        checkSumProductSingly(node.next, i + 1);
    }

    // Global variabel for doubly tjek
    static boolean allValidDoubly;

    // Rekursiv check doubly linked list, print kun gyldige tripletter, sæt allValidDoubly false ved fejl
    static void checkSumProductDoubly(DoublyNode node, int i) {
        if (node == null || node.next == null || node.next.next == null) return;

        int x = node.value;
        int y = node.next.value;
        int z = node.next.next.value;

        boolean valid = false;

        if (x + y == z) {
            System.out.printf("Element [%d] plus element [%d] giver samme som element [%d]%n", i, i + 1, i + 2);
            valid = true;
        }
        if (x * y == z) {
            System.out.printf("Element [%d] gange element [%d] giver samme som element [%d]%n", i, i + 1, i + 2);
            valid = true;
        }

        if (!valid) allValidDoubly = false;

        checkSumProductDoubly(node.next, i + 1);
    }

    public static void main(String[] args) {
        // Singly linked list eksempel
        SinglyLinkedList sList = new SinglyLinkedList();
        int[] sValues = {1, 2, 4, 5, 15, 20};
        for (int v : sValues) sList.add(v);

        System.out.println("Singly Linked List:");
        sList.print();

        allValidSingly = true;
        System.out.println("Sum/Gange check:");
        checkSumProductSingly(sList.head, 0);
        System.out.println("Reglen gælder for hele listen: " + allValidSingly);

        System.out.println();

        // Doubly linked list eksempel
        DoublyLinkedList dList = new DoublyLinkedList();
        int[] dValues = {1, 2, 3, 5, 15, 20};
        for (int v : dValues) dList.add(v);

        System.out.println("Doubly Linked List:");
        dList.print();

        allValidDoubly = true;
        System.out.println("Sum/Gange check:");
        checkSumProductDoubly(dList.head, 0);
        System.out.println("Reglen gælder for hele listen: " + allValidDoubly);
    }
}
