package MoreQA.HashTable;

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
                = new HashToNotEmptyIndexQuadraticProbing
                (11);

        // -1 is empty
        // Pre-fill the hash table with given values
        qph.hashTable[0] = 22;

        qph.hashTable[5] = 5;
        qph.hashTable[6] = 16;
        qph.hashTable[9] = 27;

        // Display the initial state of the hash table
        System.out.println("Initial hash table:");
        qph.display();

        // Insert a new element that hashes to index 1
        int newValue = 1;
        int newValue2 = 12;
        int newValue3 = 23;
        int newValue4 = 22;
        int newValue5 = 123;

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