package MoreQA.HashTable;

import java.util.Arrays;

public class HopscotchHashTable {
    private static final int SIZE = 16; // Size of the hash table
    private Integer[] table; // Hash table to store values
    private int[] hops; // Hops array to track hop distances

    public HopscotchHashTable() {
        table = new Integer[SIZE];
        hops = new int[SIZE];
        Arrays.fill(table, null); // Initialize table to null
    }

    private int hash(int key) {
        return key % SIZE; // Simple hash function
    }

    public void insert(int value) {
        int index = hash(value);
        int originalIndex = index;
        int hop = 0;

        while (true) {
            // If the spot is empty, insert the value
            if (table[index] == null) {
                table[index] = value;
                updateHops(originalIndex, hop);
                return;
            }

            // If the value is already present, do not insert duplicates
            if (table[index].equals(value)) {
                return;
            }

            // If the spot is occupied, perform hopping
            hop++;
            if (hop >= SIZE) {
                System.out.println("Table is full, could not insert: " + value);
                return;
            }

            // Move to the next index for probing
            index = (originalIndex + hop) % SIZE;

            // Check if we need to update the hop distance
            if (hops[originalIndex] < 4) { // Allow max of 4 hops
                table[index] = value; // "Insert" at this index
                updateHops(originalIndex, hop);
                return;
            }
        }
    }

    private void updateHops(int originalIndex, int hop) {
        for (int i = 0; i <= hop; i++) {
            int hopIndex = (originalIndex + i) % SIZE;
            hops[hopIndex] = Math.max(hops[hopIndex], hop);
        }
    }

    public void printTable() {
        for (int i = 0; i < SIZE; i++) {
            System.out.printf("Index %d: %s, Hops: %d\n", i, table[i], hops[i]);
        }
    }

    public static void main(String[] args) {
        HopscotchHashTable hashTable = new HopscotchHashTable();

        // Insert values according to the problem statement
        int[] values = {44000, 45000, 46000, 47000, 48000, 49000, 50000, 51000, 52000, 53000, 54000, 55000, 56000, 57000};

        for (int value : values) {
            hashTable.insert(value);
        }

        hashTable.printTable(); // Output the table

        // Simulate next insertion that hashes to index 50
        hashTable.insert(50000); // This value will hash to index 50
        hashTable.printTable(); // Output the table again to see the result
    }
}
