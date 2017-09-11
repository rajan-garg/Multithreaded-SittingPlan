package com.example.rajan.seatingplan;

/**
 * Created by root on 11/9/17.
 */

public class Utils {

    public static boolean isInteger(String str) {
        try {
            Integer i = Integer.parseInt(str);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
}
