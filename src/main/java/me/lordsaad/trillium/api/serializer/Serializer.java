package me.lordsaad.trillium.api.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public abstract class Serializer<T> {
    public static final Serializer<Location> LOCATION = new Serializer<Location>() {
        public String serialize(Location l) {
            if (l == null) {
                return null;
            }
            return new StringBuilder(l.getWorld().getName()).append('~').append(l.getX()).append('~').append(l.getY()).append('~').append(l.getZ()).append('~').append(l.getYaw()).append('~').append(l.getPitch()).toString();
        }

        public Location deserialize(String in) {
            if (in == null) {
                return null;
            }
            String[] split = in.split("~");
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
    };
    
    public abstract T deserialize(String in);
    public abstract String serialize(T in);
}
