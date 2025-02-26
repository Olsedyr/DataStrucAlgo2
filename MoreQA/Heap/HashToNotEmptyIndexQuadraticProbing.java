package MoreQA.Heap;

import MoreQA.HashTable.QuadraticProbingHashTable;

public class HashToNotEmptyIndexQuadraticProbing {
    private int[] hashTable;
    private int tableSize;

    // Constructor to initialize the hash table
    public HashToNotEmptyIndexQuadraticProbing(int size) {
        tableSize = size;
        hashTable = new int[tableSize];
        // Initialize all positions to -1 to indicate they are vacant
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = -1;
        }
    }

    // Function to insert a value into the hash table
    public void insert(int value) {
        int hash = value % tableSize;
        int i = 0;
        int newIndex = hash;

        // Quadratic probing to find the next available slot
        while (hashTable[newIndex] != -1) {
            i++;
            newIndex = (hash + i * i) % tableSize;
            if (i == tableSize) {
                System.out.println("Hash table is full");
                return;
            }
        }

        // Insert the value at the calculated index
        hashTable[newIndex] = value;
    }

    // Function to display the hash table
    public void display() {
        for (int i = 0; i < tableSize; i++) {
            System.out.print(hashTable[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        //Set Inital size here index 0
        HashToNotEmptyIndexQuadraticProbing qph
                = new HashToNotEmptyIndexQuadraticProbing(16);

        // -1 is empty
        // Pre-fill the hash table with given values
        qph.hashTable[0] = 68;
        qph.hashTable[1] = 72;
        qph.hashTable[2] = 86;
        qph.hashTable[5] = 80;
        qph.hashTable[7] = 88;
        qph.hashTable[8] = 69;

        // Display the initial state of the hash table
        System.out.println("Initial hash table:");
        qph.display();

        // Insert a new element that hashes to index 1
        int newValue = 78;
        int newValue2 = 79;
        int newValue3 = 80;
        int newValue4 = 81;
        int newValue5 = 82;

        //Insert elements
        qph.insert(newValue);
        qph.insert(newValue2);
        qph.insert(newValue3);
        qph.insert(newValue4);
        qph.insert(newValue5);


        // Display the hash table after insertion
        System.out.println("Hash table after insertion:");
        qph.display();
    }
}