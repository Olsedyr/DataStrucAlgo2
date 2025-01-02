
public class LinearProbingHashTable {
        private String[] table;
        private int size;

        public LinearProbingHashTable(int size) {
            this.size = size;
            this.table = new String[size];
        }

        // Hash function
        private int hash(char key) {
            int k = key - 'A' + 1; // Convert character to its position in the alphabet
            return (11 * k) % size;
        }

        // Insert method with linear probing
        public void insert(char key) {
            int index = hash(key);
            while (table[index] != null) {
                index = (index + 1) % size; // Linear probing
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
            LinearProbingHashTable hashTable = new LinearProbingHashTable(16);
            char[] keys = {'D', 'E', 'M', 'O', 'C', 'R', 'A', 'T'};
            for (char key : keys) {
                hashTable.insert(key);
            }
            hashTable.printTable();
        }
    }




