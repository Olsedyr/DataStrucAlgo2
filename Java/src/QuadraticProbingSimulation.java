public class QuadraticProbingSimulation {
    public static void main(String[] args) {
        final int TABLE_SIZE = 11;
        String[] table = new String[TABLE_SIZE];
        int hashIndex = 3;  // alle elementer hasher til indeks 3
        int elementCount = 0;

        // For at markere rækkefølge: Y1, Y2, Y3, ...
        //13 er antal elementer i tabellen


        for (int i = 1; i <= 13; i++) {
            int probe = 0;
            int index;
            boolean placed = false;
            while (probe < TABLE_SIZE) {
                index = (hashIndex + probe * probe) % TABLE_SIZE;
                if (table[index] == null) {
                    table[index] = "Y" + i;
                    placed = true;
                    elementCount++;
                    break;
                }
                probe++;
            }
            if (!placed) {
                System.out.println("Ingen plads til Y" + i + ", rehashing er nødvendig.");
                break;
            }
        }

        // Udskriv tabellens tilstand
        for (int i = 0; i < TABLE_SIZE; i++) {
            System.out.printf("Indeks %2d: %s\n", i, (table[i] == null ? "(ledig)" : table[i]));
        }
    }
}
