import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Aktivitet {
    private int event;
    private String task;
    private int duration;

    public Aktivitet(int event, String task, int duration) {
        this.event = event;
        this.task = task;
        this.duration = duration;
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

class FindBiggestSlackCriticalPath {
    public static void main(String[] args) {
        List<Aktivitet> tabel = new ArrayList<>();
        String aktivitetFile = "data.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(aktivitetFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] data = line.split(";");
                Aktivitet aktivitet = new Aktivitet(
                        Integer.parseInt(data[0]),
                        data[1],
                        Integer.parseInt(data[2])
                );
                tabel.add(aktivitet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalDuration = 0;
        for (Aktivitet akt : tabel)
            totalDuration += akt.getDuration();

        // --- Beregn slæk ---
        // Først: Find for hvert event den maksimale varighed
        Map<Integer, Integer> eventMaxDuration = new HashMap<>();
        for (Aktivitet akt : tabel) {
            int evt = akt.getEvent();
            int dur = akt.getDuration();
            eventMaxDuration.put(evt, Math.max(eventMaxDuration.getOrDefault(evt, 0), dur));
        }

        // Dernæst: Beregn slæk for hver aktivitet
        int maxSlack = 0;
        String aktivitetMedMaxSlack = "";
        for (Aktivitet akt : tabel) {
            int available = eventMaxDuration.get(akt.getEvent());
            int slack = available - akt.getDuration();
            if (slack > maxSlack) {
                maxSlack = slack;
                aktivitetMedMaxSlack = akt.getTask();
            }
        }

        System.out.println("Antal aktiviteter:      " + tabel.size());
        System.out.println("Gennemsnitlig varighed: " + (float) totalDuration / tabel.size());
        System.out.println("Aktivitet med det største slæk: " + aktivitetMedMaxSlack +
                " med slæk: " + maxSlack);

        // --- Beregn kritisk vej ---
        // Forudsætning: aktiviteterne er sorteret efter event
        String kritiskVej = "";
        int laengdeKritiskVej = 0;
        int aktuelEvent = tabel.get(0).getEvent();
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
            kritiskVej += maxTask + " ";
            maxVarighedAktuelEvent = 0;
            maxTask = "";
            if (indeks == tabel.size())
                break;
            aktuelEvent = tabel.get(indeks).getEvent();
        }

        System.out.println("Længde af kritisk vej:  " + laengdeKritiskVej);
        System.out.println("Kritisk vej:            " + kritiskVej);
    }
}
