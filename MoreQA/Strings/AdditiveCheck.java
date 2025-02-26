package MoreQA.Strings;

public class AdditiveCheck {

    //Algoritmen returnerer true,
    // hvis parameteren indeholder en substring af
    // tre på hinanden efterfølgende tal,
    // hvor det  tredje ciffer er lig med summen af de
    // to forrige.



    // Rekursiv metode til at kontrollere, om strengen indeholder en additiv sekvens
    public static boolean additive(String s) {
        return additiveHelper(s, 0);
    }

    // Hjælpemetode til rekursivt at kontrollere additiv sekvens
    private static boolean additiveHelper(String s, int index) {
        // Basis tilfælde: Hvis der ikke er nok cifre tilbage til at danne en sekvens
        if (index > s.length() - 3) {
            return false;
        }

        // Hent de tre på hinanden følgende cifre
        int first = s.charAt(index) - '0';
        int second = s.charAt(index + 1) - '0';
        int third = s.charAt(index + 2) - '0';

        // Kontroller, om tredje ciffer er lig med summen af de to forrige
        if (third == first + second) {
            return true;
        }

        // Fortsæt rekursionen med næste indeks
        return additiveHelper(s, index + 1);
    }

    public static void main(String[] args) {
        String s = "82842605";
        boolean result = additive(s);
        System.out.println("Resultat: " + result); // Skal udskrive true
    }
}