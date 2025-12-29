package BallsIntoBins;

import java.util.Random;

public class BallsIntoBinsN2 {
    public static void main(String[] args) {
        int numberOfBalls = 10000; // N bolde
        int numberOfBins = numberOfBalls * numberOfBalls; // M = N^2

        // Initialiser beholdere
        int[] bins = new int[numberOfBins];
        Random random = new Random();

        // Placer bolde i beholdere
        for (int i = 0; i < numberOfBalls; i++) {
            int chosenBin = random.nextInt(numberOfBins);
            bins[chosenBin]++;
        }

        // Tæl antallet af overfyldte beholdere
        int overflowCount = 0;
        for (int bin : bins) {
            if (bin > 1) {
                overflowCount++;
            }
        }

        // Print resultater
        System.out.println("Antal overfyldte beholdere: " + overflowCount);
        System.out.println("Sandsynligheden for at ingen beholder indeholder mere end én bold: " +
                (1.0 - (double)overflowCount / numberOfBins));
    }
}

//3. “Balls into Bins” med
//𝑛
//2
//n
//2
// beholdere
//
//Her har vi mange flere beholdere end bolde (kvadratisk flere).
//
//Chancen for at flere bolde havner i samme beholder (overfyldte beholdere) er meget lille.
//
//Sandsynligheden for ingen overfyldte beholdere er høj.
//
//Ulempe: Kræver meget mere plads (pladskrævende), fordi man bruger mange flere beholdere.
//
//Fordel: Meget lav risiko for kollisioner (overfyldninger), men ineffektivt rumligt.