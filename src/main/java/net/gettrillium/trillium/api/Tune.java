package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Utils;
import net.gettrillium.trillium.api.player.TrilliumPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;

public class Tune {

    protected ArrayList<String> tune;

    public Tune(String name) {
        tune = Utils.readFileLines(new File(TrilliumAPI.getInstance().getDataFolder() + "/Tunes/" + name + ".txt"));
    }

    public void play() {
        final int index = tune.size();

        new BukkitRunnable() {
            int queue = 0;

            public void run() {
                if (queue < index) {
                    queue++;
                    for (String note : tune) {
                        int dots = note.length() - note.replace(".", "").length();
                        if (dots <= 5 && dots > 0) {
                            String tone = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.TONE);
                            if (StringUtils.isAlpha(tone) && tone.length() == 1) {
                                if (tone.equalsIgnoreCase("A")
                                        || tone.equalsIgnoreCase("B")
                                        || tone.equalsIgnoreCase("C")
                                        || tone.equalsIgnoreCase("D")
                                        || tone.equalsIgnoreCase("E")
                                        || tone.equalsIgnoreCase("F")
                                        || tone.equalsIgnoreCase("G")) {
                                    if (TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.NOTE).equalsIgnoreCase("NATURAL")) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            p.playNote(p.getLocation(),
                                                    Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.INSTRUMENT).toUpperCase()),
                                                    Note.natural(dots, Note.Tone.valueOf(tone)));
                                        }
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
                        if (dots <= 8 && dots > 0) {
                            String tone = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.TONE);
                            if (tone.equalsIgnoreCase("A")
                                    || tone.equalsIgnoreCase("B")
                                    || tone.equalsIgnoreCase("C")
                                    || tone.equalsIgnoreCase("D")
                                    || tone.equalsIgnoreCase("E")
                                    || tone.equalsIgnoreCase("F")
                                    || tone.equalsIgnoreCase("G")) {
                                if (TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.NOTE).equalsIgnoreCase("NATURAL")) {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.INSTRUMENT).toUpperCase()),
                                            Note.natural(dots / 5, Note.Tone.valueOf(tone)));
                                } else if (TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.NOTE).equalsIgnoreCase("FLAT")) {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.INSTRUMENT).toUpperCase()),
                                            Note.flat(dots / 5, Note.Tone.valueOf(tone)));
                                } else if (TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.NOTE).equalsIgnoreCase("SHARP")) {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.INSTRUMENT).toUpperCase()),
                                            Note.sharp(dots / 5, Note.Tone.valueOf(tone)));
                                } else {
                                    p.playNote(p.getLocation(),
                                            Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.INSTRUMENT).toUpperCase()),
                                            Note.natural(dots / 5, Note.Tone.valueOf(tone)));
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
                        if (dots <= 5 && dots > 0) {
                            String tone = TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.TONE);
                            if (StringUtils.isAlpha(tone) && tone.length() == 1) {
                                if (tone.equalsIgnoreCase("A")
                                        || tone.equalsIgnoreCase("B")
                                        || tone.equalsIgnoreCase("C")
                                        || tone.equalsIgnoreCase("D")
                                        || tone.equalsIgnoreCase("E")
                                        || tone.equalsIgnoreCase("F")
                                        || tone.equalsIgnoreCase("G")) {
                                    if (TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.NOTE).equalsIgnoreCase("NATURAL")) {
                                        p.getProxy().playNote(p.getProxy().getLocation(),
                                                Instrument.valueOf(TrilliumAPI.getInstance().getConfig().getString(Configuration.Server.INSTRUMENT).toUpperCase()),
                                                Note.natural(dots, Note.Tone.valueOf(tone)));
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
