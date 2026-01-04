import java.util.*;

public class FindMajority {

    // ====== ITERATIV METODE (Simple, let at forstå) ======
    public static int findMajorityIterative(int[] votes, double thresholdPercent) {
        if (votes == null || votes.length == 0) {
            return -1;
        }

        int totalVotes = votes.length;
        double requiredVotes = totalVotes * (thresholdPercent / 100.0);

        // Find alle unikke kandidater
        Set<Integer> candidates = new HashSet<>();
        for (int vote : votes) {
            candidates.add(vote);
        }

        // Tæl stemmer for hver kandidat
        for (int candidate : candidates) {
            int count = 0;
            for (int vote : votes) {
                if (vote == candidate) {
                    count++;
                }
            }

            // Tjek om kandidaten har flertal
            if (count > requiredVotes) {
                return candidate;
            }
        }

        return -1;
    }

    // ====== REKURSIV METODE (Deler problemet op) ======
    public static int findMajorityRecursive(int[] votes, double thresholdPercent) {
        if (votes == null || votes.length == 0) {
            return -1;
        }

        int totalVotes = votes.length;
        double requiredVotes = totalVotes * (thresholdPercent / 100.0);

        // Find unikke kandidater rekursivt
        Set<Integer> candidates = findUniqueCandidatesRecursive(votes, 0, new HashSet<>());

        // Tjek hver kandidat rekursivt
        return checkCandidatesRecursive(votes, candidates.iterator(), requiredVotes, 0);
    }

    // Rekursiv hjælpefunktion til at finde unikke kandidater
    private static Set<Integer> findUniqueCandidatesRecursive(int[] votes, int index, Set<Integer> candidates) {
        if (index >= votes.length) {
            return candidates;
        }

        candidates.add(votes[index]);
        return findUniqueCandidatesRecursive(votes, index + 1, candidates);
    }

    // Rekursiv hjælpefunktion til at tjekke kandidater
    private static int checkCandidatesRecursive(int[] votes, Iterator<Integer> candidateIterator,
                                                double requiredVotes, int currentCount) {
        if (!candidateIterator.hasNext()) {
            return -1; // Ingen flere kandidater at tjekke
        }

        int candidate = candidateIterator.next();
        int count = countVotesRecursive(votes, candidate, 0, 0);

        if (count > requiredVotes) {
            return candidate;
        }

        return checkCandidatesRecursive(votes, candidateIterator, requiredVotes, currentCount);
    }

    // Rekursiv hjælpefunktion til at tælle stemmer for en kandidat
    private static int countVotesRecursive(int[] votes, int candidate, int index, int count) {
        if (index >= votes.length) {
            return count;
        }

        if (votes[index] == candidate) {
            count++;
        }

        return countVotesRecursive(votes, candidate, index + 1, count);
    }

    // ====== ALTERNATIV REKURSIV METODE (Divide & Conquer) ======
    public static int findMajorityDivideConquer(int[] votes, double thresholdPercent) {
        if (votes == null || votes.length == 0) {
            return -1;
        }

        int totalVotes = votes.length;
        double requiredVotes = totalVotes * (thresholdPercent / 100.0);

        // Brug Divide & Conquer til at finde kandidat med flest stemmer
        CandidateResult result = findMajorityDivideConquerHelper(votes, 0, votes.length - 1);

        // Tjek om vinderen har nok stemmer
        if (result != null && result.count > requiredVotes) {
            return result.candidate;
        }

        return -1;
    }

    // Struktur til at returnere kandidat og antal stemmer
    private static class CandidateResult {
        int candidate;
        int count;

        CandidateResult(int candidate, int count) {
            this.candidate = candidate;
            this.count = count;
        }
    }

    // Divide & Conquer hjælpefunktion
    private static CandidateResult findMajorityDivideConquerHelper(int[] votes, int left, int right) {
        // Base case: kun et element
        if (left == right) {
            return new CandidateResult(votes[left], 1);
        }

        // Del arrayet i to
        int mid = left + (right - left) / 2;

        // Løs venstre og højre del rekursivt
        CandidateResult leftResult = findMajorityDivideConquerHelper(votes, left, mid);
        CandidateResult rightResult = findMajorityDivideConquerHelper(votes, mid + 1, right);

        // Hvis venstre og højre har samme kandidat
        if (leftResult.candidate == rightResult.candidate) {
            return new CandidateResult(leftResult.candidate, leftResult.count + rightResult.count);
        }

        // Tæl hvor mange gange hver kandidat forekommer i modstanderens del
        int leftCountInRight = countInRange(votes, leftResult.candidate, mid + 1, right);
        int rightCountInLeft = countInRange(votes, rightResult.candidate, left, mid);

        // Vælg kandidaten med flest stemmer
        int totalLeftCount = leftResult.count + leftCountInRight;
        int totalRightCount = rightResult.count + rightCountInLeft;

        if (totalLeftCount > totalRightCount) {
            return new CandidateResult(leftResult.candidate, totalLeftCount);
        } else {
            return new CandidateResult(rightResult.candidate, totalRightCount);
        }
    }

    // Hjælpefunktion til at tælle forekomster i et interval
    private static int countInRange(int[] votes, int candidate, int start, int end) {
        int count = 0;
        for (int i = start; i <= end; i++) {
            if (votes[i] == candidate) {
                count++;
            }
        }
        return count;
    }

    // ====== SIMPEL TEST ======
    public static void main(String[] args) {
        // Din oprindelige opgave
        int[] votes = {7, 4, 3, 5, 3, 1, 6, 4, 5, 1, 7, 5};
        double threshold = 50.0;

        System.out.println("Stemmer: " + Arrays.toString(votes));
        System.out.println("Antal stemmer: " + votes.length);
        System.out.println("Krav: " + threshold + "% (" +
                Math.round(votes.length * threshold / 100.0) + " stemmer)");

        System.out.println("\n--- Iterativ metode ---");
        int resultIterative = findMajorityIterative(votes, threshold);
        System.out.println("Resultat: " + (resultIterative != -1 ? "Kandidat " + resultIterative : "Ingen flertal"));

        System.out.println("\n--- Rekursiv metode ---");
        int resultRecursive = findMajorityRecursive(votes, threshold);
        System.out.println("Resultat: " + (resultRecursive != -1 ? "Kandidat " + resultRecursive : "Ingen flertal"));

        System.out.println("\n--- Divide & Conquer rekursiv metode ---");
        int resultDivideConquer = findMajorityDivideConquer(votes, threshold);
        System.out.println("Resultat: " + (resultDivideConquer != -1 ? "Kandidat " + resultDivideConquer : "Ingen flertal"));

        // Test med et eksempel hvor der er en vinder
        System.out.println("\n\n=== TEST MED VINDER ===");
        int[] votesWithWinner = {1, 2, 1, 1, 3, 1, 2, 1, 1, 4}; // 1 har 6/10 = 60%
        System.out.println("Stemmer: " + Arrays.toString(votesWithWinner));

        System.out.println("\n--- Iterativ metode (50%) ---");
        int winnerIterative = findMajorityIterative(votesWithWinner, 50);
        System.out.println("Resultat: " + (winnerIterative != -1 ? "Kandidat " + winnerIterative : "Ingen flertal"));

        System.out.println("\n--- Iterativ metode (55%) ---");
        int winnerIterative55 = findMajorityIterative(votesWithWinner, 55);
        System.out.println("Resultat: " + (winnerIterative55 != -1 ? "Kandidat " + winnerIterative55 : "Ingen flertal"));

        // Analysér tidskompleksitet
        System.out.println("\n\n=== TIDSKOMPLEKSITETSANALYSE ===");
        System.out.println("Iterativ metode:");
        System.out.println("  - Finder unikke kandidater: O(n)");
        System.out.println("  - Tæller stemmer for hver kandidat: O(n × m) hvor m er antal unikke kandidater");
        System.out.println("  - I værste tilfælde: O(n²) hvis alle stemmer er forskellige");
        System.out.println("  - I bedste tilfælde: O(n) hvis der kun er én kandidat");

        System.out.println("\nRekursiv metode:");
        System.out.println("  - Rekursion dybde: O(n) for tællemekanismen");
        System.out.println("  - Kompleksitet: O(n × m)");
        System.out.println("  - Ekstra plads til rekursionsstack: O(n)");

        System.out.println("\nDivide & Conquer rekursiv metode:");
        System.out.println("  - Rekursion dybde: O(log n)");
        System.out.println("  - Kompleksitet: O(n log n)");
        System.out.println("  - Mere effektiv for store input");
    }

    // ====== EKSTRA: BOYER-MOORE ALGORITME (Alternativ iterativ) ======
    public static int findMajorityBoyerMoore(int[] votes, double thresholdPercent) {
        if (votes == null || votes.length == 0) {
            return -1;
        }

        int totalVotes = votes.length;
        double requiredVotes = totalVotes * (thresholdPercent / 100.0);

        // Fase 1: Find mulig kandidat
        int candidate = votes[0];
        int count = 1;

        for (int i = 1; i < votes.length; i++) {
            if (votes[i] == candidate) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    candidate = votes[i];
                    count = 1;
                }
            }
        }

        // Fase 2: Tjek om kandidaten har flertal
        int candidateCount = 0;
        for (int vote : votes) {
            if (vote == candidate) {
                candidateCount++;
            }
        }

        if (candidateCount > requiredVotes) {
            return candidate;
        }

        return -1;
    }
}