package MoreQA.arrays;

import java.util.HashMap;
import java.util.Map;

public class Over50PercentOccurence {

    // Metode til at finde kandidaten med mere end 50 % af stemmerne
    public static int findMajorityCandidate(int[] votes) {
        int totalVotes = votes.length;
        int majorityThreshold = totalVotes / 2; //If it was 30% totalVotes*0.3

        // Opret en HashMap til at tælle stemmerne for hver kandidat
        Map<Integer, Integer> voteCount = new HashMap<>();

        // Tæl stemmerne for hver kandidat
        for (int vote : votes) {
            voteCount.put(vote, voteCount.getOrDefault(vote, 0) + 1);
        }

        // Find kandidaten med mere end 50 % af stemmerne
        for (Map.Entry<Integer, Integer> entry : voteCount.entrySet()) {
            if (entry.getValue() > majorityThreshold) {
                return entry.getKey();
            }
        }

        // Ingen kandidat fik mere end 50 % af stemmerne
        return -1;
    }

    public static void main(String[] args) {
        int[] votes = {7, 4, 3, 5, 3, 1, 6, 4, 5, 1, 7, 5};
        int result = findMajorityCandidate(votes);
        System.out.println("Kandidaten med mere end 50 % af stemmerne: " + result); // Skal udskrive -1
    }
}

