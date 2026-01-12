public class DivideAndConcurMergeSort {

    public static void main(String[] args) {
        LinkedList list = new LinkedList();

        // Tilføj elementer (kan ændres dynamisk)
        list.add(4);
        list.add(2);
        list.add(8);
        list.add(5);
        list.add(1);
        list.add(7);

        System.out.println("Original List:");
        list.print();

        // Sorter listen med step-by-step print
        System.out.println("\nStarter Merge Sort:");
        list.head = list.mergeSort(list.head);

        System.out.println("\nSorterede List:");
        list.print();

        // Test remove rekursivt
        System.out.println("\nFjerner element 5:");
        list.head = list.remove(list.head, 5);
        list.print();

        System.out.println("\nFjerner element 42 (findes ikke):");
        list.head = list.remove(list.head, 42);
        list.print();
    }

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    static class LinkedList {
        Node head;

        // Rekursiv add: tilføj til slutningen
        public void add(int data) {
            head = addRecursive(head, data);
        }

        private Node addRecursive(Node current, int data) {
            if (current == null) {
                System.out.println("Tilføjer " + data);
                return new Node(data);
            }
            current.next = addRecursive(current.next, data);
            return current;
        }

        // Rekursiv print
        public void print() {
            printRecursive(head);
            System.out.println();
        }

        private void printRecursive(Node node) {
            if (node == null) return;
            System.out.print(node.data + " ");
            printRecursive(node.next);
        }

        // Rekursiv remove: fjern første forekomst af data
        public Node remove(Node node, int data) {
            if (node == null) {
                System.out.println("Element " + data + " ikke fundet");
                return null;
            }
            if (node.data == data) {
                System.out.println("Fjerner " + data);
                return node.next; // "Spring over" denne node
            }
            node.next = remove(node.next, data);
            return node;
        }

        // Merge Sort funktion - rekursiv
        public Node mergeSort(Node head) {
            if (head == null || head.next == null) {
                return head;
            }

            Node middle = getMiddle(head);
            Node nextOfMiddle = middle.next;
            middle.next = null; // split

            System.out.print("Splitter i: ");
            printPartialList(head);
            System.out.print(" og ");
            printPartialList(nextOfMiddle);
            System.out.println();

            Node left = mergeSort(head);
            Node right = mergeSort(nextOfMiddle);

            Node sorted = sortedMerge(left, right);

            System.out.print("Merge: ");
            printPartialList(sorted);
            System.out.println();
            return sorted;
        }

        // Find midten af listen (slow/fast pointer)
        private Node getMiddle(Node head) {
            if (head == null) return head;
            Node slow = head;
            Node fast = head.next;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }

        // Merge to sorterede lister rekursivt
        private Node sortedMerge(Node a, Node b) {
            if (a == null) return b;
            if (b == null) return a;

            if (a.data <= b.data) {
                a.next = sortedMerge(a.next, b);
                return a;
            } else {
                b.next = sortedMerge(a, b.next);
                return b;
            }
        }

        // Hjælpefunktion: print en del af listen (fra given node)
        private void printPartialList(Node node) {
            while (node != null) {
                System.out.print(node.data + " ");
                node = node.next;
            }
        }
    }
}
