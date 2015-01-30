package me.lordsaad.trillium;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saad on 29-Jan-15.
 */
public class Utils {

    public static List<Location> upanimatedcircle(Location loc1, double radius, double dots) {
        ArrayList<Location> l = new ArrayList<>();
        for (int i = 0; i < 360; i += 360/dots) {
            double y = 0;
            y = y + 1;
            double angle = (i * Math.PI / 180);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location loc2 = loc1.add(x, y, z);
            l.add(loc2);
        }
        return l;
    }

    public static List<Location> downanimatedcircle(Location loc1, double radius, double dots) {
        ArrayList<Location> l = new ArrayList<>();
        for (int i = 0; i < 360; i += 360/dots) {
            double y = 360;
            y = y - 1;
            double angle = (i * Math.PI / 180);
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Location loc2 = loc1.add(x, y, z);
            l.add(loc2);
        }
        return l;
    }

    public static List<Location> square(Location loc1, double size, double dots) {
        ArrayList<Location> l = new ArrayList<>();
        for (double x = -size; x < size; x = x + dots) {
            Location loc2 = loc1.add(x, 1, 1);
            l.add(loc2);
        }
        return l;
    }

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
