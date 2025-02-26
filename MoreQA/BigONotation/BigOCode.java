package MoreQA.BigONotation;
import java.util.*;

//Indsæt kode du vil have store O for

public class BigOCode {

    public static long bigOh(double N)
    {
        long x = 0; long y = 0;
        for (int i = 0; i < N; i++) //for-loop nummer 1
        {
            for (int j = 0; j < Math.pow(Math.log(N),2); j++)
// log er ln (den naturlige logaritme)
            {
                for (int k = 0; k <= Math.sqrt(N); k++)
                {
                    x++;
                }
            }
            i += i;
        }
        for (long k = 0; k < N*Math.sqrt(N); k++) //for-loop nummer 2
            y++;
        System.out.println(x+" "+y);
        return x+y;
    }
}
