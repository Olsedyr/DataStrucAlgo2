package MoreQA.BigONotation;
import java.util.*;

//Indsæt kode du vil have store O for

public class BigOCode {

    public static long myM(int N)
    {
        long x = 0;
        for (int i = 0; i < 10000; i++)
        {
            for (int j = 0; j < N*10; j++)
            {
                for (int k = N; k > 0; k = k/10)
                {
                    x++;
                }
            }
        }
        return x;
    }
}
