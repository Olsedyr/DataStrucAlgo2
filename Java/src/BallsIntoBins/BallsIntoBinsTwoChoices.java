package BallsIntoBins;

import java.util.Random;

public class BallsIntoBinsTwoChoices {
    public static void main(String[] args) {
        int[] trials = {65536, 65536}; // Antal bolde og beholdere
        for (int n : trials) {
            simulate(n);
        }
    }

    private static void simulate(int n) {
        int[] bins = new int[n];
        Random random = new Random();

        // Placer bolde i beholdere med to valg
        for (int i = 0; i < n; i++) {
            int chosenBin1 = random.nextInt(n);
            int chosenBin2 = random.nextInt(n);

            // Vælg den beholder med færrest bolde
            if (bins[chosenBin1] <= bins[chosenBin2]) {
                bins[chosenBin1]++;
            } else {
                bins[chosenBin2]++;
            }
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

//“Balls into Bins” med to valg – “Power of Two Choices”
//
//For hver bold vælger man to beholdere tilfældigt.
//
//Bolden placeres i den af de to, som har færrest bolde.
//
//Denne simple ændring har stor effekt:
//
//Maksimum belastning (antallet af bolde i den mest fyldte beholder) bliver dramatisk lavere.
//
//Belastningen bliver meget mere jævnt fordelt.
//
//Fordel: Ved at bruge kun to valg (i stedet for én) opnår man næsten optimal fordeling, næsten ligesom hvis man kunne se alle beholdere.
//
//Dette er et ekstremt effektivt trick i load balancing, caching, hashing osv.

//power of two choices “bryder” det tilfældige mønster ved at give lidt information og vælge det bedste af to valg, hvilket drastisk forbedrer den maksimale belastning.

//1 choise: max load = ln(N) / ln ln(N)
//2 choise: max load = ln ln(N)

//as N grows:
//1 choise: Imbalance grows quickly
//2 choise: Imbalance grows extremly slow