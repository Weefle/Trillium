package net.gettrillium.trillium.api.serializer;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer extends Serializer<Location> {
    public String serialize(Location l) {
        if (l == null) {
            return null;
        }
        return l.getWorld().getName() + '~' + l.getX() + '~' + l.getY() + '~' + l.getZ() + '~' + l.getYaw() + '~' + l.getPitch();
    }

    public Location deserialize(String in) {
        if (in == null) {
            return null;
        }
        String[] split = in.split("~");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
