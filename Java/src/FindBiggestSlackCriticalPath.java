import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Klasse der repræsenterer en aktivitet/projektopgave
 * En aktivitet har et event/knude, et navn og en varighed
 */
class Aktivitet {
    private int event;      // Knudepunkt/hændelsesnummer
    private String task;    // Opgavens navn
    private int duration;   // Varighed i tidsenheder

    public Aktivitet() {}

    public Aktivitet(int e, String t, int d) {
        event = e;
        task = t;
        duration = d;
    }

    // Get-metoder - vigtigt at have disse til dataadgang
    public int getEvent() {
        return event;
    }

    public String getTask() {
        return task;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return event + ";" + task + ";" + duration;
    }
}

/**
 * Hovedklasse der implementerer kritisk vej-analyse med slack-beregning
 * Dette er en typisk eksamensopgave i projektstyring/operationsanalyse
 */
public class FindBiggestSlackCriticalPath {

    /**
     * Hovedmetode - koordinerer hele programmets udførelse
     */
    public static void main(String[] args) {
        // Trin 1: Indlæs data fra fil
        List<Aktivitet> aktiviteter = indlaesAktiviteterFraFil();

        if (aktiviteter.isEmpty()) {
            System.out.println("Ingen data til at fortsætte. Program afsluttes.");
            return;
        }

        // Trin 2: Sorter aktiviteter (gør analysen nemmere)
        sorterAktiviteter(aktiviteter);

        // Trin 3: Udskriv indlæste data (til validering)
        udskrivAktiviteter("Indlæste og sorterede aktiviteter:", aktiviteter);

        // Trin 4: Beregn og vis basale statistikker
        visStatistikker(aktiviteter);

        // Trin 5: Find den kritiske vej (original funktionalitet)
        String[] kritiskVejResultat = findKritiskVej(aktiviteter);
        System.out.println("\n=== KRITISK VEJ-ANALYSE ===");
        System.out.println("Kritisk vej:            " + kritiskVejResultat[0]);
        System.out.println("Længde af kritisk vej:  " + kritiskVejResultat[1]);

        // Trin 6: Beregn slack (den nye funktionalitet)
        System.out.println("\n=== SLACK-ANALYSE ===");
        beregnOgVisSlack(aktiviteter);

        // Trin 7: Vis detaljeret slack-info pr. aktivitet
        visDetaljeretSlackInfo(aktiviteter);
    }

    /**
     * Indlæser aktiviteter fra en datafil med fejlhåndtering
     * @return Liste af aktiviteter
     */
    private static List<Aktivitet> indlaesAktiviteterFraFil() {
        List<Aktivitet> tabel = new ArrayList<>();

        // Mulige filsteder - forsøger flere steder for fleksibilitet
        String[] muligeFiler = {
                "data.txt", "data2.txt",
                "src/data.txt", "src/data2.txt",
                "../data.txt", "../data2.txt"
        };

        String valgtFil = null;

        // Find filen i et af de mulige steder
        for (String kandidat : muligeFiler) {
            java.io.File f = new java.io.File(kandidat);
            if (f.exists() && f.isFile()) {
                valgtFil = kandidat;
                System.out.println("Fandt datafil: " + f.getAbsolutePath());
                break;
            }
        }

        if (valgtFil == null) {
            System.err.println("FEJL: Kunne ikke finde datafilen!");
            System.err.println("Placer data.txt eller data2.txt i:");
            for (String s : muligeFiler) {
                System.err.println("  • " + new java.io.File(s).getAbsolutePath());
            }
            return tabel; // Returner tom liste
        }

        // Læs filen linje for linje
        try (BufferedReader br = new BufferedReader(new FileReader(valgtFil))) {
            String line;
            int linjeNummer = 0;

            while ((line = br.readLine()) != null) {
                linjeNummer++;
                line = line.trim();

                // Spring over tomme linjer
                if (line.isEmpty()) continue;

                // Forventet format: event;opgave;varighed
                String[] data = line.split(";");

                if (data.length != 3) {
                    System.err.println("Advarsel linje " + linjeNummer + ": Forkert format - " + line);
                    continue;
                }

                try {
                    int event = Integer.parseInt(data[0].trim());
                    String task = data[1].trim();
                    int duration = Integer.parseInt(data[2].trim());

                    // Valider data
                    if (event <= 0 || duration <= 0) {
                        System.err.println("Advarsel linje " + linjeNummer + ": Ugyldige værdier - " + line);
                        continue;
                    }

                    tabel.add(new Aktivitet(event, task, duration));

                } catch (NumberFormatException e) {
                    System.err.println("Advarsel linje " + linjeNummer + ": Talformat fejl - " + line);
                }
            }

        } catch (IOException e) {
            System.err.println("Fejl ved læsning af fil: " + e.getMessage());
        }

        return tabel;
    }

    /**
     * Sorterer aktiviteter efter event og derefter varighed (faldende)
     * Dette gør det nemmere at finde den længste aktivitet per event
     */
    private static void sorterAktiviteter(List<Aktivitet> aktiviteter) {
        Collections.sort(aktiviteter, new Comparator<Aktivitet>() {
            @Override
            public int compare(Aktivitet a1, Aktivitet a2) {
                // Først sorter efter event (stigende)
                int eventCompare = Integer.compare(a1.getEvent(), a2.getEvent());
                if (eventCompare != 0) {
                    return eventCompare;
                }
                // Derefter sorter efter varighed (faldende)
                return Integer.compare(a2.getDuration(), a1.getDuration());
            }
        });
    }

    /**
     * Udskriver en liste af aktiviteter med overskrift
     */
    private static void udskrivAktiviteter(String overskrift, List<Aktivitet> aktiviteter) {
        System.out.println("\n" + overskrift);
        System.out.println("Event\tAktivitet\tVarighed");
        System.out.println("-----\t---------\t--------");

        for (Aktivitet a : aktiviteter) {
            System.out.printf("%-7d%-12s%-8d%n",
                    a.getEvent(), a.getTask(), a.getDuration());
        }
    }

    /**
     * Beregner og viser grundlæggende statistikker
     */
    private static void visStatistikker(List<Aktivitet> aktiviteter) {
        int totalVarighed = 0;
        int maxEvent = 0;

        for (Aktivitet a : aktiviteter) {
            totalVarighed += a.getDuration();
            if (a.getEvent() > maxEvent) {
                maxEvent = a.getEvent();
            }
        }

        System.out.println("\n=== PROJEKTSTATISTIK ===");
        System.out.println("Antal aktiviteter:      " + aktiviteter.size());
        System.out.println("Antal events/knuder:    " + maxEvent);
        System.out.println("Total varighed:         " + totalVarighed);
        System.out.printf("Gennemsnitlig varighed: %.1f%n",
                (float) totalVarighed / aktiviteter.size());
    }

    /**
     * Finder den kritiske vej i projektet
     * Den kritiske vej er den længste vej gennem projektet
     * @return Array med [kritiskVej, længde]
     */
    private static String[] findKritiskVej(List<Aktivitet> aktiviteter) {
        StringBuilder kritiskVej = new StringBuilder();
        int laengdeKritiskVej = 0;

        int aktuelEvent = 1;
        int indeks = 0;
        int maxVarighedAktuelEvent = 0;
        String maxTask = "";

        while (true) {
            // Find den længste aktivitet for det aktuelle event
            while (indeks < aktiviteter.size() &&
                    aktiviteter.get(indeks).getEvent() == aktuelEvent) {

                Aktivitet aktuelt = aktiviteter.get(indeks);
                if (aktuelt.getDuration() > maxVarighedAktuelEvent) {
                    maxVarighedAktuelEvent = aktuelt.getDuration();
                    maxTask = aktuelt.getTask();
                }
                indeks++;
            }

            // Tilføj den længste aktivitet til den kritiske vej
            laengdeKritiskVej += maxVarighedAktuelEvent;
            kritiskVej.append(maxTask);

            // Nulstil for næste event
            maxVarighedAktuelEvent = 0;
            maxTask = "";

            // Hvis vi er færdige med alle aktiviteter
            if (indeks == aktiviteter.size()) {
                break;
            }

            // Gå til næste event
            aktuelEvent = aktiviteter.get(indeks).getEvent();
        }

        return new String[]{kritiskVej.toString(), String.valueOf(laengdeKritiskVej)};
    }

    /**
     * Beregner den maksimale varighed for hvert event
     * Dette bruges til at beregne slack
     */
    private static int[] beregnMaksVarighedPerEvent(List<Aktivitet> aktiviteter) {
        // Find det højeste event-nummer
        int maxEvent = 0;
        for (Aktivitet a : aktiviteter) {
            if (a.getEvent() > maxEvent) {
                maxEvent = a.getEvent();
            }
        }

        // Opret array til at gemme maksimal varighed per event
        int[] eventMaxDuration = new int[maxEvent + 1];

        // Beregn maksimal varighed for hvert event
        for (int event = 1; event <= maxEvent; event++) {
            int maxDur = 0;
            for (Aktivitet a : aktiviteter) {
                if (a.getEvent() == event && a.getDuration() > maxDur) {
                    maxDur = a.getDuration();
                }
            }
            eventMaxDuration[event] = maxDur;
        }

        return eventMaxDuration;
    }

    /**
     * Beregner og viser samlet slack og største slack
     */
    private static void beregnOgVisSlack(List<Aktivitet> aktiviteter) {
        int[] maksVarighedPerEvent = beregnMaksVarighedPerEvent(aktiviteter);

        int samletSlack = 0;
        int stoersteSlack = -1;
        String aktivitetMedStoersteSlack = "";

        // Beregn slack for hver aktivitet
        for (Aktivitet a : aktiviteter) {
            int slack = maksVarighedPerEvent[a.getEvent()] - a.getDuration();
            samletSlack += slack;

            // Find største slack (til ekstra info)
            if (slack > stoersteSlack) {
                stoersteSlack = slack;
                aktivitetMedStoersteSlack = a.getTask();
            }
        }

        // Udskriv resultater
        System.out.println("Samlet slæk i projektet: " + samletSlack + " tidsenheder");
        System.out.println("Største enkeltstående slæk: " + stoersteSlack +
                " tidsenheder (aktivitet " + aktivitetMedStoersteSlack + ")");

        // Vis også gennemsnitligt slack
        System.out.printf("Gennemsnitligt slæk per aktivitet: %.1f tidsenheder%n",
                (float) samletSlack / aktiviteter.size());
    }

    /**
     * Viser detaljeret slack-information for hver aktivitet
     */
    private static void visDetaljeretSlackInfo(List<Aktivitet> aktiviteter) {
        int[] maksVarighedPerEvent = beregnMaksVarighedPerEvent(aktiviteter);

        System.out.println("\n=== DETALJERET SLACK-INFO ===");
        System.out.println("Event\tAktivitet\tVarighed\tMax i Event\tSlack");
        System.out.println("-----\t---------\t--------\t-----------\t-----");

        for (Aktivitet a : aktiviteter) {
            int maxIEVENT = maksVarighedPerEvent[a.getEvent()];
            int slack = maxIEVENT - a.getDuration();

            System.out.printf("%-5d\t%-9s\t%-8d\t%-11d\t%-5d%n",
                    a.getEvent(), a.getTask(), a.getDuration(), maxIEVENT, slack);
        }
    }
}