//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CriticalPath2
{
    private int event;
    private String task;
    private int duration;

    public CriticalPath2() {}

    public CriticalPath2 (int e, String t, int d)
    {
        event = e;
        task = t;
        duration = d;
    }

    public int getEvent()
    {
        return event;
    }

    public String getTask()
    {
        return task;
    }

    public int getDuration()
    {
        return duration;
    }



    public static void main(String[] args)
    {
        List <CriticalPath2> tabel = new ArrayList<CriticalPath2>();
        String aktivitetFile = "data.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(aktivitetFile)))
        {
            String semiKolon = ";";
            String line = "";
            // Read each line from the file
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
                String[] data = line.split(semiKolon);
                CriticalPath2 aktivitet = new CriticalPath2(Integer.parseInt(data[0]),data[1],Integer.parseInt(data[2]));
                tabel.add(aktivitet);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        int totalDuration = 0;

        for (int i = 0; i < tabel.size(); i++)
            totalDuration += tabel.get(i).getDuration();

        System.out.println("Antal aktiviteter:      "+ tabel.size());
        System.out.println("Gennemsnitlig varighed: "+ (float) totalDuration / tabel.size());

        String kritiskVej = ("");
        int laengdeKritiskVej = 0;
        int noOfEvents = tabel.get(tabel.size()-1).getEvent();
        int aktuelEvent = 1;
        int indeks = 0;
        int maxVarighedAktuelEvent = 0;
        String maxTask = "";

        while (true)
        {
            while (indeks < tabel.size() && tabel.get(indeks).getEvent() == aktuelEvent)
            {
                if (maxVarighedAktuelEvent < tabel.get(indeks).getDuration())
                {
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

        System.out.println("Længde af kritisk vej:  "+laengdeKritiskVej);
        System.out.println("Kritisk vej:            "+kritiskVej);
    }
}



