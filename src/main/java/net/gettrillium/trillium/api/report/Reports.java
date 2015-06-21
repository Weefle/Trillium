package net.gettrillium.trillium.api.report;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.gettrillium.trillium.api.Utils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Reports {
    private static Table<UUID, Location, String> reports = HashBasedTable.create();

    public static void addReport(Player p, Location loc, String message) {
        reports.put(p.getUniqueId(), loc, message);
    }

    public static ArrayList<String> getReportMessages() {
        ArrayList<String> reportList = new ArrayList<>();
        for (Map.Entry<UUID, Map<Location, String>> row : reports.rowMap().entrySet()) {
            for (Map.Entry<Location, String> column : row.getValue().entrySet()) {
                if (column.getValue() != null) {
                    reportList.add(column.getValue());
                }
            }
        }
        return reportList;
    }

    public static void removeReport(String message) {
        for (Map.Entry<UUID, Map<Location, String>> row : reports.rowMap().entrySet()) {
            for (Map.Entry<Location, String> column : row.getValue().entrySet()) {
                if (column.getValue().equals(message)) {
                    reports.remove(row.getKey(), column.getKey());
                }
            }
        }
    }

    public static Table<UUID, Location, String> getReports() {
        return reports;
    }

    public static void setReports(Table<UUID, Location, String> table) {
        reports.putAll(table);
    }

    public static void save() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(ReportDatabase.report());
        try {
            yml.save(ReportDatabase.report());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getReport(int index) {
        return getReportMessages().get(index);
    }

    public static void clearReports() {
        reports.clear();
    }

    public static ArrayList<String> serialize() {
        ArrayList<String> reportList = new ArrayList<>();
        for (Map.Entry<UUID, Map<Location, String>> row : reports.rowMap().entrySet()) {
            for (Map.Entry<Location, String> column : row.getValue().entrySet()) {
                reportList.add(row.getKey() + ";" + Utils.locationToString(column.getKey()) + ";" + column.getValue());
            }
        }
        return reportList;
    }

    public static Table<UUID, Location, String> deserialize() {
        Table<UUID, Location, String> table = HashBasedTable.create();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(ReportDatabase.report());
        for (String serialized : yml.getStringList("rows")) {
            table.put(UUID.fromString(serialized.split(";")[0]), Utils.stringFromLocation(serialized.split(";")[1]), serialized.split(";")[2]);
        }
        return table;
    }
}
