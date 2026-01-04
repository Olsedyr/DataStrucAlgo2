import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList2<T> implements Iterable<T> {
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList2() {
        head = null;
        tail = null;
        size = 0;
    }

    // ====== BASIC METHODS ======

    public void addFirst(T element) {
        Node newNode = new Node(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    public void addLast(T element) {
        Node newNode = new Node(element);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        if (head == tail) {
            T data = head.data;
            head = tail = null;
            size--;
            return data;
        }

        Node current = head;
        while (current.next != tail) {
            current = current.next;
        }
        T data = tail.data;
        tail = current;
        tail.next = null;
        size--;
        return data;
    }

    public T getFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head.data;
    }

    public T getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public boolean contains(T element) {
        Node current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void printList() {
        Node current = head;
        System.out.print("LinkedList: ");
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println();
    }

    // ====== ITERATOR ======

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    // ====== DEMONSTRATION ======
    public static void main(String[] args) {
        LinkedList2<Integer> myList = new LinkedList2<>();

        myList.addFirst(10);
        myList.addLast(20);
        myList.addLast(30);
        myList.addFirst(5);

        System.out.println("Linked List:");
        myList.printList();

        System.out.println("First: " + myList.getFirst());
        System.out.println("Last: " + myList.getLast());
        System.out.println("Size: " + myList.size());

        System.out.println("\nRemoving first: " + myList.removeFirst());
        System.out.println("Removing last: " + myList.removeLast());

        myList.printList();

        System.out.println("\nUsing iterator:");
        for (Integer num : myList) {
            System.out.print(num + " ");
        }
    }
}