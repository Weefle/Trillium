package net.gettrillium.trillium.api;

import net.gettrillium.trillium.api.Configuration.Server;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Tune {

    protected List<String> tune;

    public Tune(String name) {
        try {
            for (Object line : FileUtils.readLines(new File(TrilliumAPI.getInstance().getDataFolder() + "/Tunes/" + name + ".txt"))) {
                String s = String.valueOf(line);
                tune.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        final int index = tune.size();

        new BukkitRunnable() {
            int queue = 0;

            @Override
            public void run() {
                if (queue > index) {
                    return;
                }

                queue++;

                for (String note : tune) {
                    int dots = note.length();
                    if ((dots <= 5) && (dots > 0)) {
                        String tone = TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_TONE);
                        if (StringUtils.isAlpha(tone) && (tone.length() == 1)) {
                            if (tone.equalsIgnoreCase("A")
                                    || tone.equalsIgnoreCase("B")
                                    || tone.equalsIgnoreCase("C")
                                    || tone.equalsIgnoreCase("D")
                                    || tone.equalsIgnoreCase("E")
                                    || tone.equalsIgnoreCase("F")
                                    || tone.equalsIgnoreCase("G")) {
                                if (TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_NOTE).equalsIgnoreCase("NATURAL")) {
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.playNote(p.getLocation(),
                                                Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_INSTRUMENT).toUpperCase()),
                                                Note.natural(dots, Tone.valueOf(tone)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(TrilliumAPI.getInstance(), 0, 1);
    }

    public void play(final Player p) {
        final int index = tune.size();

        for (String s : tune) {
            Bukkit.broadcastMessage(s);
        }

        new BukkitRunnable() {
            int queue = 0;

            public void run() {
                if (queue < index) {
                    queue++;

                    for (String note : tune) {
                        int dots = note.length() - note.replace(".", "").length();
                        if ((dots <= 8) && (dots > 0)) {
                            String tone = TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_TONE);
                            if (tone.equalsIgnoreCase("A")
                                    || tone.equalsIgnoreCase("B")
                                    || tone.equalsIgnoreCase("C")
                                    || tone.equalsIgnoreCase("D")
                                    || tone.equalsIgnoreCase("E")
                                    || tone.equalsIgnoreCase("F")
                                    || tone.equalsIgnoreCase("G")) {
                                if (TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_NOTE).equalsIgnoreCase("NATURAL")) {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_INSTRUMENT).toUpperCase()),
                                            Note.natural(dots / 5, Tone.valueOf(tone)));
                                } else if (TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_NOTE).equalsIgnoreCase("FLAT")) {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_INSTRUMENT).toUpperCase()),
                                            Note.flat(dots / 5, Tone.valueOf(tone)));
                                } else if (TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_NOTE).equalsIgnoreCase("SHARP")) {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_INSTRUMENT).toUpperCase()),
                                            Note.sharp(dots / 5, Tone.valueOf(tone)));
                                } else {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_INSTRUMENT).toUpperCase()),
                                            Note.natural(dots / 5, Tone.valueOf(tone)));
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(TrilliumAPI.getInstance(), 0, 1);
    }

    public void play(final TrilliumPlayer p) {
        final int index = tune.size();

        new BukkitRunnable() {
            int queue = 0;

            public void run() {
                if (queue < index) {
                    queue++;
                    for (String note : tune) {
                        int dots = note.length() - note.replace(".", "").length();
                        if ((dots <= 5) && (dots > 0)) {
                            String tone = TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_TONE);
                            if (StringUtils.isAlpha(tone) && (tone.length() == 1)) {
                                if (tone.equalsIgnoreCase("A")
                                        || tone.equalsIgnoreCase("B")
                                        || tone.equalsIgnoreCase("C")
                                        || tone.equalsIgnoreCase("D")
                                        || tone.equalsIgnoreCase("E")
                                        || tone.equalsIgnoreCase("F")
                                        || tone.equalsIgnoreCase("G")) {
                                    if (TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_NOTE).equalsIgnoreCase("NATURAL")) {
                                        p.getProxy().playNote(p.getProxy().getLocation(),
                                                Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Server.TUNE_INSTRUMENT).toUpperCase()),
                                                Note.natural(dots, Tone.valueOf(tone)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(TrilliumAPI.getInstance(), 0, 10);
    }
}
