package MoreQA.HashTable;

import java.util.Arrays;

public class ToHashTabeller {
    private String[] table;
    private int size;

    public ToHashTabeller(int size) {
        this.size = size;
        this.table = new String[size];
        Arrays.fill(this.table, null);
    }

    private int hash(char key) {
        return (11 * (key - 'A' + 1)) % size;
    }

    public void insertLinear(char key) {
        int index = hash(key);
        while (table[index] != null) {
            index = (index + 1) % size;
        }
        table[index] = String.valueOf(key);
    }

    public void insertQuadratic(char key) {
        int index = hash(key);
        int i = 0;
        while (table[(index + i * i) % size] != null) {
            i++;
        }
        table[(index + i * i) % size] = String.valueOf(key);
    }

    public static void main(String[] args) {
        ToHashTabeller linearTable = new ToHashTabeller(16);
        for (char c : "DEMOCRAT".toCharArray()) {
            linearTable.insertLinear(c);
        }
        System.out.println("Linear probing udført.");

        ToHashTabeller quadraticTable = new ToHashTabeller(16);
        for (char c : "REPUBLICAN".toCharArray()) {
            quadraticTable.insertQuadratic(c);
        }
        System.out.println("Quadratic probing udført.");
    }
}