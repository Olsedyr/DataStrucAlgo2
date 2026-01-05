import java.util.*;

public class QuadraticProbingAnalyzer {

    public static void main(String[] args) {
        // INPUT - Ændr kun her
        int capacity = 17;
        int commonHash = 5;

        // Indtast både bogstaver OG deres positioner
        Map<Character, Integer> input = new LinkedHashMap<>();
        input.put('G', 3);
        input.put('F', 4);
        input.put('C', 5);
        input.put('L', 6);
        input.put('P', 8);
        input.put('U', 9);
        input.put('Y', 13);
        input.put('M', 14);

        analyze(capacity, commonHash, input);
    }

    public static void analyze(int capacity, int commonHash, Map<Character, Integer> letterPositions) {
        System.out.println("QUADRATIC PROBING ANALYSE");
        System.out.println("Kapacitet: " + capacity + ", Fælles hash: " + commonHash);
        System.out.println();

        // Beregn korrekte positioner via quadratic probing
        System.out.println("Quadratic probing sekvens:");
        List<Integer> correctPositions = new ArrayList<>();
        Map<Integer, Integer> probeMap = new HashMap<>();

        for (int i = 0; correctPositions.size() < letterPositions.size(); i++) {
            int pos = (commonHash + i * i) % capacity;
            if (!correctPositions.contains(pos)) {
                correctPositions.add(pos);
                probeMap.put(pos, i);
                System.out.printf("i=%d: (%d + %d²) mod %d = %d%n",
                        i, commonHash, i, capacity, pos);
            }
        }

        System.out.println("\nKorrekte positioner for " + letterPositions.size() + " elementer:");
        System.out.println(correctPositions);

        // Find forkerte placeringer
        List<Character> wrongLetters = new ArrayList<>();
        List<Integer> wrongPositions = new ArrayList<>();
        List<Integer> missingPositions = new ArrayList<>(correctPositions);

        System.out.println("\nAnalyse af inputtede placeringer:");
        for (Map.Entry<Character, Integer> entry : letterPositions.entrySet()) {
            char letter = entry.getKey();
            int position = entry.getValue();

            if (correctPositions.contains(position)) {
                missingPositions.remove(Integer.valueOf(position));
                System.out.printf("%c på %d: Korrekt%n", letter, position);
            } else {
                wrongLetters.add(letter);
                wrongPositions.add(position);
                System.out.printf("%c på %d: Forkert!%n", letter, position);
            }
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("KONKLUSION:");

        if (wrongLetters.isEmpty()) {
            System.out.println("Alle bogstaver er korrekt placeret!");
        } else {
            for (int i = 0; i < wrongLetters.size(); i++) {
                char wrongLetter = wrongLetters.get(i);
                int wrongPosition = wrongPositions.get(i);

                if (!missingPositions.isEmpty()) {
                    int correctPosition = missingPositions.get(i);
                    int probe = probeMap.get(correctPosition);

                    System.out.printf("%n'%c' er forkert placeret på index %d%n",
                            wrongLetter, wrongPosition);
                    System.out.printf("Skal være på index %d%n", correctPosition);
                    System.out.printf("Beregning: (%d + %d²) mod %d = %d%n",
                            commonHash, probe, capacity, correctPosition);
                }
            }
        }

        // Vis korrekt tabel
        System.out.println("\n" + "=".repeat(50));
        System.out.println("KORREKT TABEL:");

        char[] table = new char[capacity];
        Arrays.fill(table, '-');

        // Placer alle bogstaver korrekt
        Map<Character, Integer> correctPlacement = new HashMap<>(letterPositions);

        // Ret de forkerte
        for (int i = 0; i < wrongLetters.size(); i++) {
            if (i < missingPositions.size()) {
                correctPlacement.put(wrongLetters.get(i), missingPositions.get(i));
            }
        }

        // Fyld tabellen
        for (Map.Entry<Character, Integer> entry : correctPlacement.entrySet()) {
            table[entry.getValue()] = entry.getKey();
        }

        // Print tabel
        System.out.print("Indeks: ");
        for (int i = 0; i < capacity; i++) {
            System.out.printf("%3d", i);
        }

        System.out.print("\nVærdi:  ");
        for (int i = 0; i < capacity; i++) {
            System.out.printf("%3c", table[i]);
        }
        System.out.println();
    }
}