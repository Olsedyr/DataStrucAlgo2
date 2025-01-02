public class QuadraticProbingHashTable2 {
    private String[] table;
    private int size;

    public QuadraticProbingHashTable2(int size) {
        this.size = size;
        this.table = new String[size];
    }

    // Hash function
    private int hash(char key) {
        int k = key - 'A' + 1; // Convert character to its position in the alphabet
        return (11 * k) % size;
    }

    // Insert method with quadratic probing
    public void insert(char key) {
        int index = hash(key);
        int i = 0;
        while (table[index] != null) {
            i++;
            index = (hash(key) + i * i) % size; // Quadratic probing
        }
        table[index] = String.valueOf(key);
    }

    // Print the hash table
    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.println(i + ": " + table[i]);
        }
    }

    public static void main(String[] args) {
        QuadraticProbingHashTable hashTable = new QuadraticProbingHashTable(16);
        char[] keys = {'R', 'E', 'P', 'U', 'B', 'L', 'I', 'C', 'A', 'N'};
        for (char key : keys) {
            hashTable.insert(key);
        }
        hashTable.printTable();
    }
}