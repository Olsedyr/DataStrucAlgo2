// CompleteLinkedListsExamVersion.java
// Indeholder alt til eksamen:
// - Singly Linked List (insert, delete, reverse, palindrome)
// - Doubly Linked List (insert, delete, reverse, palindrome)
// - Generics → understøtter String, Character, Integer, osv.
//
public class LinkedListPalindrome {

    /* ============================================================
       1. SINGLY LINKED LIST (GENERIC)
       ============================================================ */
    static class SinglyNode<T> {
        T value;
        SinglyNode<T> next;

        SinglyNode(T value) { this.value = value; }
    }

    static class SinglyLinkedList<T> {
        SinglyNode<T> head;

        /* ---------------- ADD (append) ---------------- */
        public void add(T value) {
            SinglyNode<T> node = new SinglyNode<>(value);
            if (head == null) {
                head = node;
                return;
            }
            SinglyNode<T> curr = head;
            while (curr.next != null) curr = curr.next;
            curr.next = node;
        }

        /* ---------------- INSERT at index ---------------- */
        public void insert(int index, T value) {
            SinglyNode<T> node = new SinglyNode<>(value);

            if (index == 0) {
                node.next = head;
                head = node;
                return;
            }

            SinglyNode<T> curr = head;
            for (int i = 0; curr != null && i < index - 1; i++)
                curr = curr.next;

            if (curr == null) return; // index out of bounds

            node.next = curr.next;
            curr.next = node;
        }

        /* ---------------- DELETE first occurrence ---------------- */
        public void delete(T value) {
            if (head == null) return;

            if (head.value.equals(value)) {
                head = head.next;
                return;
            }

            SinglyNode<T> curr = head;
            while (curr.next != null && !curr.next.value.equals(value))
                curr = curr.next;

            if (curr.next != null)
                curr.next = curr.next.next;
        }

        /* ---------------- REVERSE (recursion) ---------------- */
        public void reverse() {
            head = reverseRec(head);
        }

        private SinglyNode<T> reverseRec(SinglyNode<T> node) {
            if (node == null || node.next == null) return node;
            SinglyNode<T> newHead = reverseRec(node.next);
            node.next.next = node;
            node.next = null;
            return newHead;
        }

        /* ---------------- PRINT ---------------- */
        public void print() {
            SinglyNode<T> curr = head;
            while (curr != null) {
                System.out.print(curr.value + " -> ");
                curr = curr.next;
            }
            System.out.println("null");
        }
    }

    /* ============================================================
       PALINDROME CHECK (Singly)
       ============================================================ */
    static class Left<T> {
        SinglyNode<T> node;
        Left(SinglyNode<T> node) { this.node = node; }
    }

    public static <T> boolean isPalindrome(SinglyNode<T> head) {
        return isPalRec(new Left<>(head), head);
    }

    private static <T> boolean isPalRec(Left<T> left, SinglyNode<T> right) {
        if (right == null) return true;

        boolean ok = isPalRec(left, right.next);
        if (!ok) return false;

        boolean match = left.node.value.equals(right.value);
        left.node = left.node.next;

        return match;
    }


    /* ============================================================
       2. DOUBLY LINKED LIST (GENERIC)
       ============================================================ */
    static class DoublyNode<T> {
        T value;
        DoublyNode<T> next;
        DoublyNode<T> prev;

        DoublyNode(T value) { this.value = value; }
    }

    static class DoublyLinkedList<T> {
        DoublyNode<T> head;
        DoublyNode<T> tail;

        /* ---------------- ADD (append) ---------------- */
        public void add(T value) {
            DoublyNode<T> node = new DoublyNode<>(value);
            if (head == null) {
                head = tail = node;
                return;
            }
            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        /* ---------------- INSERT at index ---------------- */
        public void insert(int index, T value) {
            DoublyNode<T> node = new DoublyNode<>(value);

            if (index == 0) {
                node.next = head;
                if (head != null) head.prev = node;
                head = node;
                if (tail == null) tail = node;
                return;
            }

            DoublyNode<T> curr = head;
            for (int i = 0; curr != null && i < index - 1; i++)
                curr = curr.next;

            if (curr == null) return;

            node.next = curr.next;
            node.prev = curr;
            if (curr.next != null) curr.next.prev = node;
            else tail = node;
            curr.next = node;
        }

        /* ---------------- DELETE first occurrence ---------------- */
        public void delete(T value) {
            DoublyNode<T> curr = head;

            while (curr != null && !curr.value.equals(value))
                curr = curr.next;

            if (curr == null) return;

            if (curr.prev != null) curr.prev.next = curr.next;
            else head = curr.next;

            if (curr.next != null) curr.next.prev = curr.prev;
            else tail = curr.prev;
        }

        /* ---------------- REVERSE (backwards traversal) ---------------- */
        public void reversePrint() {
            DoublyNode<T> curr = tail;
            while (curr != null) {
                System.out.print(curr.value + " <-> ");
                curr = curr.prev;
            }
            System.out.println("null");
        }

        /* ---------------- PRINT ---------------- */
        public void print() {
            DoublyNode<T> curr = head;
            while (curr != null) {
                System.out.print(curr.value + " <-> ");
                curr = curr.next;
            }
            System.out.println("null");
        }
    }

    /* ============================================================
       PALINDROME CHECK (Doubly)
       ============================================================ */
    public static <T> boolean isPalindrome(DoublyNode<T> head, DoublyNode<T> tail) {
        DoublyNode<T> left = head;
        DoublyNode<T> right = tail;

        while (left != null && right != null && left != right && left.prev != right) {
            if (!left.value.equals(right.value)) return false;
            left = left.next;
            right = right.prev;
        }
        return true;
    }

    /* ============================================================
       MAIN — DEMO
       ============================================================ */
    public static void main(String[] args) {

        System.out.println("===== SINGLY LINKED LIST =====");
        SinglyLinkedList<Character> sList = new SinglyLinkedList<>();
        sList.add('r');
        sList.add('a');
        sList.add('c');
        sList.add('e');
        sList.add('c');
        sList.add('a');
        sList.add('r');

        sList.print();
        System.out.println("Palindrome? " + isPalindrome(sList.head));
        System.out.println("  ");

        sList.delete('e');
        System.out.print("Efter delete('e'): ");
        System.out.println("  ");
        System.out.println("Palindrome? " + isPalindrome(sList.head));
        sList.print();
        System.out.println("  ");

        sList.insert(1, 'Z');
        System.out.print("Efter insert(1,'Z'): ");
        System.out.println("  ");
        System.out.println("Palindrome? " + isPalindrome(sList.head));
        sList.print();
        System.out.println("  ");

        sList.reverse();
        System.out.print("Efter reverse(): ");
        System.out.println("  ");
        System.out.println("Palindrome? " + isPalindrome(sList.head));
        sList.print();
        System.out.println("  ");


        System.out.println("\n===== DOUBLY LINKED LIST =====");
        DoublyLinkedList<String> dList = new DoublyLinkedList<>();
        dList.add("A");
        dList.add("B");
        dList.add("C");
        dList.add("B");
        dList.add("A");

        dList.print();
        System.out.println("Palindrome? " + isPalindrome(dList.head, dList.tail));
        System.out.println("  ");

        dList.delete("C");
        System.out.print("Efter delete('C'): ");
        System.out.println("  ");
        dList.print();
        System.out.println("Palindrome? " + isPalindrome(dList.head, dList.tail));
        System.out.println("  ");

        dList.insert(1, "X");
        System.out.print("Efter insert(1,'X'): ");
        System.out.println("  ");
        dList.print();
        System.out.println("Palindrome? " + isPalindrome(dList.head, dList.tail));
        System.out.println("  ");

        System.out.print("Reverse traversal: ");
        System.out.println("  ");
        dList.reversePrint();
        System.out.println("Palindrome? " + isPalindrome(dList.head, dList.tail));
        System.out.println("  ");
    }
}
