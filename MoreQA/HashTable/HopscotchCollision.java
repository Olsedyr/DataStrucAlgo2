package MoreQA.HashTable;

import java.util.Arrays;

public class HopscotchCollision {
    private static final int TABLE_SIZE = 14; // Table size to cover the range 0-13
    private static final int HOP_RANGE = 4; // Maximum allowed hops
    private int[] table;
    private int[] hopInfo;

    public HopscotchCollision() {
        table = new int[TABLE_SIZE];
        hopInfo = new int[TABLE_SIZE];
        Arrays.fill(table, -1); // Initialize the table with -1 indicating empty slots
        Arrays.fill(hopInfo, 0); // Initialize hop info to 0000 for each index
    }

    // Updated hash function that maps the result to the range [0, TABLE_SIZE - 1]
    private int hash(int value) {
        return value % TABLE_SIZE; // Map to the range 0 to 13
    }

    // Insert value into the table
    public boolean insert(int value) {
        int index = hash(value);
        int startIndex = index;
        System.out.println("Inserting value: " + value);
        System.out.println("Hash index: " + index);

        // Check if the slot is available
        for (int i = 0; i < HOP_RANGE; i++) {
            int newIndex = (index + i) % TABLE_SIZE; // Wrap around using modulo
            if (table[newIndex] == -1) { // Empty slot found
                table[newIndex] = value;
                updateHopInfo(startIndex, newIndex);
                System.out.println("Inserted value " + value + " at index " + newIndex);
                printTable();
                return true;
            } else {
                System.out.println("Slot occupied at index " + newIndex + ", trying to hop.");
            }
        }

        // If no slot is found within the hop range, explain the problem
        System.out.println("Failed to insert: " + value + " - No available hopscotch range.");
        System.out.println("Problem: The hash index " + index + " and its hop range (4 slots) are fully occupied.");
        System.out.println("This means the new insertion cannot find an available slot within the allowed hop range.");
        System.out.println("As a result, the insertion fails, and the table cannot accommodate the new value.");
        printTable();
        return false;
    }

    // Update hop information (track where value was moved to)
    private void updateHopInfo(int start, int newIndex) {
        int hopDistance = (newIndex - start + TABLE_SIZE) % TABLE_SIZE; // Ensure it's positive
        if (hopDistance < HOP_RANGE) {
            hopInfo[start] |= (1 << hopDistance); // Update hop info
        } else {
            System.out.println("Warning: " + newIndex + " is outside the hop range for " + start);
        }
    }

    // Delete value at a specific index
    public boolean delete(int value) {
        int index = hash(value);
        System.out.println("Attempting to delete value: " + value);

        // Find the index of the value and remove it
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (table[i] == value) {
                table[i] = -1; // Remove value
                hopInfo[i] = 0; // Reset hop info
                System.out.println("Deleted value " + value + " at index " + i);
                printTable();
                return true;
            }
        }
        System.out.println("Value " + value + " not found for deletion.");
        return false;
    }

    // Print the table and hop info in a readable format
    public void printTable() {
        System.out.println("Index\tValue\tHop");
        for (int i = 0; i < TABLE_SIZE; i++) {
            System.out.printf("%d\t%d\t%s\n", i, table[i], Integer.toBinaryString(hopInfo[i]));
        }
    }

    public static void main(String[] args) {
        HopscotchCollision hashTable = new HopscotchCollision();
        int[] values = {47, 51, 55, 51, 46, 51, 49, 53, 50, 51};

        // Inserting values
        for (int value : values) {
            hashTable.insert(value);
        }

        // Example of deleting a value
        hashTable.delete(51); // Deleting value 51 from the table

        // Example of inserting a value after deletion
        hashTable.insert(50); // Inserting value 50 after deletion

        // Simulate the problem when the next insertion hashes to index 50
        System.out.println("\nSimulating the problem when the next insertion hashes to index 50:");
        hashTable.insert(50); // This will fail and explain the problem
    }
}