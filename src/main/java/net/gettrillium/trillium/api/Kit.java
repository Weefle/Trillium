package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Trillium;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Kit {

    private String name;

    public Kit(String kitname) {
        this.name = kitname;
    }

    public void giveTo(Player p) {
        for (ItemStack is : getItems()) {
            p.getInventory().addItem(is);
        }
    }

    public ArrayList<ItemStack> getItems() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        Trillium tr = TrilliumAPI.getInstance();

        for (String items : tr.getConfig().getConfigurationSection(Configuration.Kits.KIT_MAKER + this.name + ".items").getKeys(false)) {

            if (items.equalsIgnoreCase("written_book")) {

                ItemStack stack = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) stack.getItemMeta();

                for (String data : tr.getConfig().getConfigurationSection(Configuration.Kits.KIT_MAKER + this.name + ".items." + items).getKeys(false)) {

                    if (data.equalsIgnoreCase("book-file")) {

                        File book = new File(tr.getDataFolder() + "/Books/" + tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".book-file") + ".txt");
                        if (book.exists()) {
                            meta.setPages(Utils.convertFileToBookPages(book));
                        } else {
                            tr.getLogger().severe("Kitss: Found 'written_book' but could not find book directory of '" + book.getName() + "' directory. Ignoring...");
                        }
                    }

                    if (data.equalsIgnoreCase("title")) {
                        meta.setTitle(ChatColor.translateAlternateColorCodes('&', tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".title")));
                    }

                    if (data.equalsIgnoreCase("author")) {
                        meta.setAuthor(ChatColor.translateAlternateColorCodes('%', tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".author")));
                    }

                    if (data.equalsIgnoreCase("lore")) {
                        if (data.equalsIgnoreCase("lore")) {
                            List<String> lore = new ArrayList<>();
                            for (String lores : tr.getConfig().getStringList(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".lore")) {
                                lore.add(ChatColor.translateAlternateColorCodes('&', lores));
                            }
                            meta.setLore(lore);
                        }
                    }
                }
                stack.setItemMeta(meta);
                stacks.add(stack);
            } else {
                ItemStack stack = new ItemStack(Material.valueOf(items.toUpperCase()));
                ItemMeta meta = stack.getItemMeta();

                for (String data : tr.getConfig().getConfigurationSection(Configuration.Kits.KIT_MAKER + this.name + ".items." + items).getKeys(false)) {

                    if (data.equalsIgnoreCase("name")) {
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".name")));
                    }

                    if (data.equalsIgnoreCase("durability")) {
                        if (StringUtils.isNumeric(tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".durability"))) {
                            stack.setDurability((short) Integer.parseInt(tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".durability")));
                        } else {
                            tr.getLogger().severe("Kitss: Durability of item: '" + items + "' is not an integer! Ignoring...");
                        }
                    }

                    if (data.equalsIgnoreCase("lore")) {
                        List<String> lore = new ArrayList<>();
                        for (String lores : tr.getConfig().getStringList(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".lore")) {
                            lore.add(ChatColor.translateAlternateColorCodes('&', lores));
                        }
                        meta.setLore(lore);
                    }

                    if (data.equalsIgnoreCase("enchantments")) {
                        List<String> enchs = tr.getConfig().getStringList(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".enchantments");
                        for (String enchlevel : enchs) {
                            Enchantment enchantment = Enchantment.getByName(enchlevel.split(":")[0]);
                            int level = Integer.parseInt(enchlevel.split(":")[1]);
                            meta.addEnchant(enchantment, level, true);
                        }
                    }

                    if (data.equalsIgnoreCase("amount")) {
                        if (StringUtils.isNumeric(tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".amount"))) {
                            stack.setAmount(Integer.parseInt(tr.getConfig().getString(Configuration.Kits.KIT_MAKER + this.name + ".items." + items + ".amount")));
                        } else {
                            tr.getLogger().severe("Kitss: Amount of item: '" + items + "' is not an integer! Ignoring...");
                            stack.setAmount(1);
                        }
                    }
                }
                stack.setItemMeta(meta);
                stacks.add(stack);
            }
        }
        return stacks;
    }
}

