package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kit {

    // TODO: Add cool-down and written_book

    private int cooldown;
    private String name;

    public Kit(String kitname) {
        this.name = TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + kitname);
        this.cooldown = Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + kitname + ".cool-down"));
    }

    public void giveTo(Player p) {
        for (ItemStack is : getItems()) {
            p.getInventory().addItem(is);
        }
    }

    public List<ItemStack> getItems() {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (String items : TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Kit.KIT_MAKER + this.name + ".items")) {
            Material mat = Material.getMaterial(items);
            if (mat != null) {
                for (String data : TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Kit.KIT_MAKER + this.name + ".items." + items)) {

                    String name;
                    int durability;
                    ArrayList<String> lore = new ArrayList<>();
                    int amount = 0;

                    if (data.equalsIgnoreCase("name")) {
                        name = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".name"));
                    } else {
                        name = items;
                    }

                    if (data.equalsIgnoreCase("durability")) {
                        if (StringUtils.isNumeric(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".durability"))) {
                            durability = Integer.parseInt(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".durability"));
                        } else {
                            TrilliumAPI.getInstance().getLogger().warning("Kits: Durability of item: " + items + " is not an integer! Ignoring...");
                            durability = 0;
                        }
                    } else {
                        durability = 0;
                    }

                    if (data.equalsIgnoreCase("lore")) {
                        String s = ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".lore"));
                        if (s.contains(";")) {
                            String[] lines = s.split(";");
                            Collections.addAll(lore, lines);
                        } else {
                            lore.add(s);
                        }
                    }

                    if (data.equalsIgnoreCase("amount")) {
                        if (StringUtils.isNumeric(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".amount"))) {
                            amount = Integer.parseInt(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".amount"));
                        } else {
                            TrilliumAPI.getInstance().getLogger().warning("Kits: Amount of item: " + items + " is not an integer! Ignoring...");
                        }
                    } else {
                        amount = 1;
                    }

                    ItemStack stack = new ItemStack(mat, amount);
                    stack.setDurability((short) durability);
                    ItemMeta meta = stack.getItemMeta();
                    meta.setLore(lore);
                    meta.setDisplayName(name);
                    stack.setItemMeta(meta);
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }
}

