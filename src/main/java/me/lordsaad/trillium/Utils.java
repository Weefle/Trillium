package me.lordsaad.trillium;

/**
 * Created by saad on 29-Jan-15.
 */
public class Utils {

    public static boolean isdouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isint(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
