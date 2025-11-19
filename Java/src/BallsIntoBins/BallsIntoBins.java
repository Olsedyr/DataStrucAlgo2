package BallsIntoBins;

import java.util.Random;

public class BallsIntoBins {
    public static void main(String[] args) {
        int[] trials = {10007, 32749}; // Antal bolde og beholdere
        for (int n : trials) {
            simulate(n);
        }
    }

    private static void simulate(int n) {
        int[] bins = new int[n];
        Random random = new Random();

        // Placer bolde i beholdere
        for (int i = 0; i < n; i++) {
            int chosenBin = random.nextInt(n);
            bins[chosenBin]++;
        }

        // Beregn gennemsnit og maksimum
        double average = 0;
        int max = 0;
        for (int bin : bins) {
            average += bin;
            if (bin > max) {
                max = bin;
            }
        }
        average /= n; // Gennemsnit

        // Print resultater
        System.out.println("For " + n + " bolde og beholdere:");
        System.out.println("Gennemsnitligt antal bolde pr. beholder: " + average);
        System.out.println("Maksimalt antal bolde i en beholder: " + max);
        System.out.println();
    }
}
