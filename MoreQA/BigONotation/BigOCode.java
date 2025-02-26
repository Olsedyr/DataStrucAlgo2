package MoreQA.BigONotation;
import java.util.*;

//Indsæt kode du vil have store O for

public class BigOCode {

    public static int myMethod( int N )     {        int x = 0;        for (int i = 0; i < N; i++)             for (int j = 0; j < N/2; j++)                 for (int k = 1; k < N;)                 {                     x++;                     k *= 2;                 }        return x;     }
}
