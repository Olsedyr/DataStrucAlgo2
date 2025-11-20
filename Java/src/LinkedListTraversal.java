public class LinkedListTraversal {
    // Node klasse til linked list
        static class Node {
            int data;
            Node next;

            Node(int data) {
                this.data = data;
                this.next = null;
            }
        }

        // Rekursiv traversal: udskriv data i alle noder
        public static void traverse(Node node) {
            if (node == null) {
                return; // Basis case: slut på listen
            }
            System.out.print(node.data + " ");
            traverse(node.next);  // rekursivt kald på næste node
        }

        public static void main(String[] args) {
            // Opret linked list: 1 -> 2 -> 3 -> 4 -> null
            Node head = new Node(1);
            head.next = new Node(2);
            head.next.next = new Node(3);
            head.next.next.next = new Node(4);

            System.out.print("Linked list traversal: ");
            traverse(head);
            System.out.println();
        }
}




