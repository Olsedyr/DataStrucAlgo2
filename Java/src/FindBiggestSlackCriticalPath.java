import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Aktivitet {
    private int event;
    private String task;
    private int duration;

    public Aktivitet() {}

    public Aktivitet(int e, String t, int d) {
        event = e;
        task = t;
        duration = d;
    }

    public int getEvent() {
        return event;
    }

    public String getTask() {
        return task;
    }

    public int getDuration() {
        return duration;
    }
}

public class FindBiggestSlackCriticalPath {
    public static void main(String[] args) {
        List<Aktivitet> tabel = new ArrayList<>();

        // Mulige filnavne/steder – tilpas hvis din fil hedder noget andet
        String[] muligeFiler = {
                "data.txt",
                "data2.txt",
                "src/data.txt",
                "src/data2.txt",
                "../data.txt",
                "../data2.txt"
        };

        String valgtFil = null;

        // Prøv at finde filen
        for (String kandidat : muligeFiler) {
            java.io.File f = new java.io.File(kandidat);
            if (f.exists() && f.isFile()) {
                valgtFil = kandidat;
                System.out.println("Fandt datafil: " + f.getAbsolutePath());
                break;
            }
        }

        if (valgtFil == null) {
            System.err.println("KUNNE IKKE FINDE DATAFILEN!");
            System.err.println("Prøvede følgende steder:");
            for (String s : muligeFiler) {
                System.err.println("  - " + new java.io.File(s).getAbsolutePath());
            }
            System.err.println("\nLæg venligst data.txt eller data2.txt i en af disse mapper.");
            return; // Stop programmet pænt
        }

        // Læs filen
        try (BufferedReader br = new BufferedReader(new FileReader(valgtFil))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                System.out.println(line); // Bevar udskrivning af linjer
                String[] data = line.split(";");
                if (data.length != 3) {
                    System.out.println("Ugyldig linje (ignoreres): " + line);
                    continue;
                }
                Aktivitet aktivitet = new Aktivitet(
                        Integer.parseInt(data[0].trim()),
                        data[1].trim(),
                        Integer.parseInt(data[2].trim())
                );
                tabel.add(aktivitet);
            }
        } catch (IOException e) {
            System.err.println("Fejl ved læsning af fil: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (tabel.isEmpty()) {
            System.out.println("Ingen aktiviteter læst fra filen – tjek formatet.");
            return;
        }

        // ----- DEN UÆNDREDE ORIGINAL KODE FRA UDDLEVERET MATERIALE -----
        int totalDuration = 0;
        for (int i = 0; i < tabel.size(); i++)
            totalDuration += tabel.get(i).getDuration();

        System.out.println("Antal aktiviteter:      " + tabel.size());
        System.out.println("Gennemsnitlig varighed: " + (float) totalDuration / tabel.size());

        String kritiskVej = "";
        int laengdeKritiskVej = 0;
        int noOfEvents = tabel.get(tabel.size() - 1).getEvent();
        int aktuelEvent = 1;
        int indeks = 0;
        int maxVarighedAktuelEvent = 0;
        String maxTask = "";

        while (true) {
            while (indeks < tabel.size() && tabel.get(indeks).getEvent() == aktuelEvent) {
                if (maxVarighedAktuelEvent < tabel.get(indeks).getDuration()) {
                    maxVarighedAktuelEvent = tabel.get(indeks).getDuration();
                    maxTask = tabel.get(indeks).getTask();
                }
                indeks++;
            }
            laengdeKritiskVej += maxVarighedAktuelEvent;
            kritiskVej += maxTask;
            maxVarighedAktuelEvent = 0;
            maxTask = "";
            if (indeks == tabel.size())
                break;
            aktuelEvent = tabel.get(indeks).getEvent();
        }

        System.out.println("Længde af kritisk vej:  " + laengdeKritiskVej);
        System.out.println("Kritisk vej:            " + kritiskVej);

        // ----- DIN TILFØJELSE: STØRSTE SLACK -----
        int maxEvent = 0;
        for (Aktivitet a : tabel) {
            if (a.getEvent() > maxEvent) maxEvent = a.getEvent();
        }

        int[] eventMaxDuration = new int[maxEvent + 1];

        for (int event = 1; event <= maxEvent; event++) {
            int maxDur = 0;
            for (Aktivitet a : tabel) {
                if (a.getEvent() == event && a.getDuration() > maxDur) {
                    maxDur = a.getDuration();
                }
            }
            eventMaxDuration[event] = maxDur;
        }

        String aktivitetMedStoersteSlack = "";
        int stoersteSlack = -1;

        for (Aktivitet a : tabel) {
            int slack = eventMaxDuration[a.getEvent()] - a.getDuration();
            if (slack > stoersteSlack) {
                stoersteSlack = slack;
                aktivitetMedStoersteSlack = a.getTask();
            }
        }

        System.out.println("\nAktivitet med største slæk:");
        System.out.println("Aktivitet " + aktivitetMedStoersteSlack + " med et slæk på " + stoersteSlack + " tidsenheder.");
    }
}

//[event];[aktivitet_navn];[varighed]