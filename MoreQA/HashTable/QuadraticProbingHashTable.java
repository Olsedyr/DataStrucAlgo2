package MoreQA.HashTable;

import java.util.Arrays;

public class QuadraticProbingHashTable {
    private int[] table;
    private int size;

    public QuadraticProbingHashTable(int size) {
        this.size = size;
        this.table = new int[size];
        Arrays.fill(table, -1); // Use -1 to indicate empty spots
    }

    // Simple hash function
    private int hash(int key) {
        return key % size;
    }

    // Insert using quadratic probing
    public void insert(int key) {
        int index = hash(key);
        int i = 0;

        // Quadratic probing loop with a slight variation
        while (i < size) {
            int newIndex = (index + i + i * i) % size;
            if (table[newIndex] == -1 || table[newIndex] == -2) { // Allow for deleted spots
                table[newIndex] = key;
                return; // Successful insertion
            }
            i++;
        }

        System.out.println("Could not insert key: " + key);
    }

    // Delete using quadratic probing
    public void delete(int key) {
        int index = hash(key);
        int i = 0;

        // Quadratic probing loop
        while (i < size) {
            int newIndex = (index + i + i * i) % size;
            if (table[newIndex] == -1) {
                System.out.println("Key not found: " + key);
                return; // Key not found, stop searching
            }
            if (table[newIndex] == key) {
                table[newIndex] = -2; // Mark as deleted
                System.out.println("Deleted key: " + key + " at index: " + newIndex);
                return; // Successful deletion
            }
            i++;
        }

        System.out.println("Key not found: " + key);
    }

    public void printTable() {
        System.out.println(Arrays.toString(table));
    }

    public static void main(String[] args) {
        QuadraticProbingHashTable hashTable = new QuadraticProbingHashTable(11); // Size 11

        // Insert elements
        int[] keys = {16, 32, 48, 64, 80, 96};  // All hash to index 0

        for (int key : keys) {
            hashTable.insert(key);
        }

        hashTable.printTable();  // Output the table

        // Delete some elements
        hashTable.delete(32);
        hashTable.delete(64);
        hashTable.printTable();  // Output the table after deletions

        System.out.println("Done");
    }
}
