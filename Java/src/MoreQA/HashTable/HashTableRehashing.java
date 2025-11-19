package MoreQA.HashTable;

import java.util.*;

public class HashTableRehashing {

    static class HashTable {
        private String[] table;
        private int size;
        private int numElements; // Track the number of elements

        public HashTable(int size) {
            this.size = size;
            table = new String[size];
            numElements = 0;
        }

        public void insert(String value, int key) {
            // Check load factor and rehash if necessary
            if ((double) numElements / size > 0.5) {
                rehash();
            }

            // Insert the new element
            int index = key % size;
            int i = 0;
            while (table[(index + i * i) % size] != null) {
                i++;
            }
            table[(index + i * i) % size] = value;
            numElements++; // Increment the number of elements
        }

        private void rehash() {
            System.out.println("Load factor exceeded 0.5. Rehashing...");

            // Determine new table size (next prime after doubling the current size)
            int newSize = getNextPrime(2 * size);

            // Create new hash table
            HashTable newHashTable = new HashTable(newSize);

            // Rehash elements
            for (String element : getElements()) {
                int key = element.charAt(0) - 'A' + 1; // Calculate key based on letter position
                newHashTable.insert(element, key);
            }

            // Update the current table and size
            this.table = newHashTable.table;
            this.size = newHashTable.size;
            this.numElements = newHashTable.numElements;
        }

        public void display() {
            for (int i = 0; i < size; i++) {
                System.out.println("Index " + i + ": " + (table[i] != null ? table[i] : ""));
            }
        }

        public List<String> getElements() {
            List<String> elements = new ArrayList<>();
            for (String value : table) {
                if (value != null) {
                    elements.add(value);
                }
            }
            return elements;
        }
    }

    public static int getNextPrime(int num) {
        while (true) {
            num++;
            if (isPrime(num)) {
                return num;
            }
        }
    }

    public static boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // Current hash table
        HashTable hashTable = new HashTable(11);
        hashTable.insert("A", 1);
        hashTable.insert("W", 23);
        hashTable.insert("C", 3);
        hashTable.insert("O", 15);
        hashTable.insert("E", 5);
        hashTable.insert("S", 19);

        System.out.println("Current Hash Table:");
        hashTable.display();

        // Insert more elements to trigger rehashing
        hashTable.insert("B", 2);
        hashTable.insert("D", 4);
        hashTable.insert("F", 6);

        System.out.println("\nHash Table after Rehashing:");
        hashTable.display();
    }
}