package me.lordsaad.trillium;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
import java.util.List;

public class Utils {

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

    public static void saveinventory(Player p, ItemStack[] items, ItemStack[] armor) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));

        ItemStack[] itemspart = p.getInventory().getContents();
        ItemStack[] armorpart = p.getInventory().getArmorContents();

        yml.set("Inventory.items", itemspart);
        yml.set("Inventory.armor", armorpart);

        try {
            yml.save(PlayerDatabase.db(p));
        } catch (IOException e) {
            e.printStackTrace();
        }

        clearInventory(p);

        p.getInventory().setContents(items);
        p.getInventory().setArmorContents(armor);
    }

    public void restoreinventory(Player p) {
        clearInventory(p);

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(PlayerDatabase.db(p));


        List<?> itemsList = null;
        List<?> armorList = null;
        itemsList = yml.getList("Inventory.items");
        armorList = yml.getList("Inventory.armor");

        if (itemsList != null && armorList != null) {

            ItemStack[] itemspart2 = itemsList.toArray(new ItemStack[itemsList.size()]);
            ItemStack[] armorpart2 = armorList.toArray(new ItemStack[armorList.size()]);

            p.getInventory().setContents(itemspart2);
            p.getInventory().setArmorContents(armorpart2);
        }
    }

    public static void clearInventory(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        InventoryView view = p.getOpenInventory();
        if (view != null) {
            view.setCursor(null);
            Inventory i = view.getTopInventory();
            if (i != null) {
                i.clear();
            }
        }
    }
}
