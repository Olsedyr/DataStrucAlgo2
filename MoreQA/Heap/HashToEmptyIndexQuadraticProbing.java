package MoreQA.Heap;

public class HashToEmptyIndexQuadraticProbing {
    private int[] hashTable;
    private int tableSize;

    // Constructor to initialize the hash table
    public HashToEmptyIndexQuadraticProbing(int size) {
        tableSize = size;
        hashTable = new int[tableSize];
        // Initialize all positions to -1 to indicate they are vacant
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = -1;
        }
    }

    // Function to insert a value into the hash table with a specified hash index
    public void insert(int value, int hashIndex) {
        int hash = hashIndex % tableSize;
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
            if (hashTable[i] == -1) {
                System.out.print("- ");
            } else {
                System.out.print(hashTable[i] + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        HashToEmptyIndexQuadraticProbing qph = new HashToEmptyIndexQuadraticProbing(11);

        // Pre-fill the hash table with given values
        qph.hashTable[2] = 86;
        qph.hashTable[3] = 82;
        qph.hashTable[6] = 80;
        qph.hashTable[8] = 69;
        qph.hashTable[10] = 70;

        // Display the initial state of the hash table
        System.out.println("Initial hash table:");
        qph.display();

        // Insert new elements with specified hash indices
        qph.insert(81, 7); // Hashes to index 7
        qph.insert(67, 8); // Hashes to index 8
        qph.insert(72, 2); // Hashes to index 2

        // Display the hash table after insertion
        System.out.println("Hash table after insertion:");
        qph.display();
    }
}