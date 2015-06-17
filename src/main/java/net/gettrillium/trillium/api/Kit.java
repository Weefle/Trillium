package net.gettrillium.trillium.api;

import net.gettrillium.trillium.Utils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Kit {

    // TODO: Add cool-down and written_book

    private int cooldown;
    private String name;

    public Kit(String kitname) {
        this.name = kitname;
        //this.cooldown = Utils.timeToTickConverter(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + kitname + ".cool-down"));
    }

    public void giveTo(Player p) {
        for (ItemStack is : getItems()) {
            p.getInventory().addItem(is);
        }
    }

    public ArrayList<ItemStack> getItems() {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (String items : TrilliumAPI.getInstance().getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER + this.name + ".items").getKeys(false)) {

            if (items.equalsIgnoreCase("written_book")) {

                ItemStack stack = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) stack.getItemMeta();

                for (String data : TrilliumAPI.getInstance().getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER + this.name + ".items." + items).getKeys(false)) {

                    if (data.equalsIgnoreCase("book-file")) {

                        File book = new File(TrilliumAPI.getInstance().getDataFolder() + "/Books/" + TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".book-file") + "/");
                        if (book.exists()) {
                            File[] pages = book.listFiles();
                            ArrayList<String> content = new ArrayList<>();
                            for (File page : pages != null ? pages : new File[0]) {
                                if (StringUtils.isNumeric(FilenameUtils.removeExtension(page.getName()))) {
                                    try {
                                        content.add(ChatColor.translateAlternateColorCodes('&', Utils.readFile(page)));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    TrilliumAPI.getInstance().getLogger().severe("Kits: Page '" + page.getName() + "' in book folder '" + book.getName() + "' is NOT an integer. Cannot create book.");
                                }
                            }
                            meta.setPages(content);
                        } else {
                            TrilliumAPI.getInstance().getLogger().severe("Kits: Found 'written_book' but could not find book directory of '" + book.getName() + "' directory. Ignoring...");
                        }
                    } else {
                        TrilliumAPI.getInstance().getLogger().severe("Kits: Found 'written_book' but no book directory was specified. Ignoring...");
                    }

                    if (data.equalsIgnoreCase("title")) {
                        meta.setTitle(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".title")));
                    } else {
                        meta.setTitle(ChatColor.RED + "<ERROR>");
                    }

                    if (data.equalsIgnoreCase("author")) {
                        meta.setAuthor(ChatColor.translateAlternateColorCodes('%', TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".author")));
                    } else {
                        meta.setAuthor(ChatColor.RED + "<ERROR>");
                    }

                    if (data.equalsIgnoreCase("lore")) {
                        if (data.equalsIgnoreCase("lore")) {
                            List<String> lore = new ArrayList<>();
                            for (String lores : TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".lore")) {
                                lore.add(ChatColor.translateAlternateColorCodes('&', lores));
                            }
                            meta.setLore(lore);
                        }
                    }

                    stack.setItemMeta(meta);
                    stacks.add(stack);

                }
            } else {
                ItemStack stack = new ItemStack(Material.valueOf(items.toUpperCase()));
                ItemMeta meta = stack.getItemMeta();

                for (String data : TrilliumAPI.getInstance().getConfig().getConfigurationSection(Configuration.Kit.KIT_MAKER + this.name + ".items." + items).getKeys(false)) {

                    if (data.equalsIgnoreCase("name")) {
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".name")));
                    }

                    if (data.equalsIgnoreCase("durability")) {
                        if (StringUtils.isNumeric(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".durability"))) {
                            stack.setDurability((short) Integer.parseInt(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".durability")));
                        } else {
                            TrilliumAPI.getInstance().getLogger().severe("Kits: Durability of item: '" + items + "' is not an integer! Ignoring...");
                        }
                    }

                    if (data.equalsIgnoreCase("lore")) {
                        List<String> lore = new ArrayList<>();
                        for (String lores : TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".lore")) {
                            lore.add(ChatColor.translateAlternateColorCodes('&', lores));
                        }
                        meta.setLore(lore);
                    }

                    if (data.equalsIgnoreCase("enchantments")) {
                        List<String> enchs = TrilliumAPI.getInstance().getConfig().getStringList(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".enchantments");
                        for (String enchlevel : enchs) {
                            Enchantment enchantment = Enchantment.getByName(enchlevel.split(":")[0]);
                            int level = Integer.parseInt(enchlevel.split(":")[1]);
                            meta.addEnchant(enchantment, level, true);
                        }
                    }

                    if (data.equalsIgnoreCase("amount")) {
                        if (StringUtils.isNumeric(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".amount"))) {
                            stack.setAmount(Integer.parseInt(TrilliumAPI.getInstance().getConfig().getString(Configuration.Kit.KIT_MAKER + this.name + ".items." + items + ".amount")));
                        } else {
                            TrilliumAPI.getInstance().getLogger().severe("Kits: Amount of item: '" + items + "' is not an integer! Ignoring...");
                            stack.setAmount(1);
                        }
                    }

                    stack.setItemMeta(meta);
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }
}

