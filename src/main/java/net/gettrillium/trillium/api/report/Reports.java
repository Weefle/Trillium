package net.gettrillium.trillium.api.report;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.Utils;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reports {
    private static Table<Integer, String, HashMap<String, Location>> reports = HashBasedTable.create();

    public static void addReport(Player p, String message, Location loc) {
        HashMap<String, Location> map = new HashMap<>();
        map.put(message, loc);
        reports.put(reports.size() + 1, p.getName(), map);
        save();
    }

    public static void removeReport(int index) {
        Table<Integer, String, HashMap<String, Location>> table = HashBasedTable.create();
        table.putAll(reports);

        for (Map.Entry<Integer, Map<String, HashMap<String, Location>>> row : table.rowMap().entrySet()) {
            if (row.getKey() == index) {
                for (Map.Entry<String, HashMap<String, Location>> value : row.getValue().entrySet()) {
                    reports.remove(row.getKey(), value.getKey());
                }
            }
        }
        save();
    }

    public static Message getReport(int index) {
        String msg = null;

        for (Map.Entry<Integer, Map<String, HashMap<String, Location>>> row : reports.rowMap().entrySet()) {
            if (row.getKey() == index) {
                for (Map.Entry<String, HashMap<String, Location>> value : row.getValue().entrySet()) {
                    for (Map.Entry<String, Location> value2 : value.getValue().entrySet()) {
                        msg = value.getKey() + " - " + value2.getKey() + ChatColor.DARK_GRAY + " - "
                                + Utils.locationToString(value2.getValue());
                    }
                }
            }
        }

        return new Message(Mood.NEUTRAL, index + "", msg);
    }

    public static ArrayList<Message> getReportMessages() {
        ArrayList<Message> format = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, HashMap<String, Location>>> row : reports.rowMap().entrySet()) {
            for (Map.Entry<String, HashMap<String, Location>> value : row.getValue().entrySet()) {
                for (Map.Entry<String, Location> value2 : value.getValue().entrySet()) {
                    format.add(new Message(Mood.NEUTRAL, row.getKey() + "",
                            value.getKey() + " - " + value2.getKey() + ChatColor.DARK_GRAY + " - "
                                    + Utils.locationToString(value2.getValue())));
                }
            }
        }

        return format;
    }

    public static void clearReports() {
        reports.clear();
    }

    public static void setReports() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(ReportDatabase.report());
        List<String> serialized = yml.getStringList("rows");
        for (String deserialize : serialized) {

            HashMap<String, Location> map = new HashMap<>();
            map.put(deserialize.split(";")[2], Utils.locationFromString(deserialize.split(";")[3]));

            reports.put(Integer.parseInt(deserialize.split(";")[0]), deserialize.split(";")[1], map);
        }
    }

    private static void save() {
        ArrayList<String> serialized = new ArrayList<>();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(ReportDatabase.report());

        for (Map.Entry<Integer, Map<String, HashMap<String, Location>>> row : reports.rowMap().entrySet()) {
            for (Map.Entry<String, HashMap<String, Location>> value : row.getValue().entrySet()) {
                for (Map.Entry<String, Location> value2 : value.getValue().entrySet()) {
                    serialized.add(row.getKey() + ";" + value.getKey() + ";" + value2.getKey() + ";"
                            + Utils.locationToString(value2.getValue()));
                }
            }
        }

        yml.set("rows", serialized);

        try {
            yml.save(ReportDatabase.report());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
