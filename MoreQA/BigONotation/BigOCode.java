package MoreQA.BigONotation;
import java.util.*;

//Indsæt kode du vil have store O for

public class BigOCode {

    public static long myMethod(int n) {
        if (n <= 1)
            return 1;
        else
            return myMethod(n-1) + myMethod(n-2);
    }
}