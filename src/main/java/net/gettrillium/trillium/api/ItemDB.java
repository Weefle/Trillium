package net.gettrillium.trillium.api;

import org.bukkit.Material;

/**
 * Created by Saad on 8/11/2015.
 */
public enum  ItemDB {

    STONE(Material.STONE, 0, "stone", "blockstone", "stoneblock", "stn");

    private Material material;
    private int durability;
    private String[] synonyms;

    ItemDB(Material material, int durability, String... synonyms) {
        this.material = material;
        this.durability = durability;
        this.synonyms = synonyms;
    }
}
