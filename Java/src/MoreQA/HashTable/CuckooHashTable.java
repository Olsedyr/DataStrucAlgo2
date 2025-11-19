package MoreQA.HashTable;

import java.util.Random;

public class CuckooHashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 11;
    private static final double MAX_LOAD_FACTOR = 0.5;

    private Entry<K, V>[] table1;
    private Entry<K, V>[] table2;
    private int size;

    public CuckooHashTable() {
        table1 = new Entry[DEFAULT_CAPACITY];
        table2 = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(K key, V value) {
        if ((double) size / table1.length > MAX_LOAD_FACTOR) {
            rehash();
        }
        insert(key, value);
    }

    private void insert(K key, V value) {
        Random random = new Random();
        Entry<K, V> newEntry = new Entry<>(key, value);
        int index1 = hash1(key);
        int index2 = hash2(key);
        int count = 0;

        while (count < table1.length) {
            if (table1[index1] == null) {
                table1[index1] = newEntry;
                size++;
                return;
            } else {
                // Swap
                Entry<K, V> temp = table1[index1]; // Initialize temp here
                table1[index1] = newEntry;
                newEntry = temp;
            }
            index2 = hash2(newEntry.key);
            if (table2[index2] == null) {
                table2[index2] = newEntry;
                size++;
                return;
            } else {
                // Swap
                Entry<K, V> temp = table2[index2]; // Initialize temp here
                table2[index2] = newEntry;
                newEntry = temp;
            }
            index1 = hash1(newEntry.key);
            count++;
        }
        throw new IllegalStateException("Too many collisions");
    }

    public V get(K key) {
        int index1 = hash1(key);
        int index2 = hash2(key);

        if (table1[index1] != null && table1[index1].key.equals(key)) {
            return table1[index1].value;
        } else if (table2[index2] != null && table2[index2].key.equals(key)) {
            return table2[index2].value;
        }
        return null; // Not found
    }

    private void rehash() {
        Entry<K, V>[] oldTable1 = table1;
        Entry<K, V>[] oldTable2 = table2;
        table1 = new Entry[oldTable1.length * 2];
        table2 = new Entry[oldTable2.length * 2];
        size = 0;

        for (Entry<K, V> entry : oldTable1) {
            if (entry != null) {
                put(entry.key, entry.value);
            }
        }

        for (Entry<K, V> entry : oldTable2) {
            if (entry != null) {
                put(entry.key, entry.value);
            }
        }
    }

    private int hash1(K key) {
        return Math.abs(key.hashCode()) % table1.length;
    }

    private int hash2(K key) {
        return (Math.abs(key.hashCode() / 2) % table2.length);
    }

    public static void main(String[] args) {
        CuckooHashTable<Integer, String> cuckooHashTable = new CuckooHashTable<>();

        // Insert some key-value pairs
        cuckooHashTable.put(1, "One");
        cuckooHashTable.put(2, "Two");
        cuckooHashTable.put(3, "Three");
        cuckooHashTable.put(4, "Four");
        cuckooHashTable.put(5, "Five");

        // Retrieve values
        System.out.println("Key 1: " + cuckooHashTable.get(1));
        System.out.println("Key 2: " + cuckooHashTable.get(2));
        System.out.println("Key 3: " + cuckooHashTable.get(3));
        System.out.println("Key 4: " + cuckooHashTable.get(4));
        System.out.println("Key 5: " + cuckooHashTable.get(5));
    }
}
