package MoreQA.BigONotation;
import java.util.*;

//Indsæt kode du vil have store O for

public class BigOCode {

    public static String myM(int N)
    {
        int x = 0, y = 0;
        for (int j = 0; j < N; j++)
        {
            for (int i = N; i > 0; i=i/3)
            {
                for (int k = N; k > 0; k=k/2)
                {
                    x++;
                }
            }
        }
        for (float v = 0; v < N; v+=Math.sqrt(0.001)) //C++: #include <math.h>
            y++;
        return x+" "+y;
    }


}
