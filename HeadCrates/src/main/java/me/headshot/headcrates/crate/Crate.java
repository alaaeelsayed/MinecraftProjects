package me.headshot.headcrates.crate;

import me.headshot.headcrates.HeadCrates;
import me.headshot.headcrates.config.HeadCratesConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class Crate {
    private List<CrateItem> crateItems;
    private String name;

    private Crate(String name, List<CrateItem> crateItems) {
        this.crateItems = crateItems;
        this.name = name;
    }

    public static Crate fromName(String name) {
        HeadCratesConfig config = HeadCrates.getInstance().getCratesConfig();
        String path = "crates." + name;
        List<CrateItem> crateItems = new ArrayList<>();
        Set<String> ids = config.getConfig().getConfigurationSection(path + ".items").getKeys(false);
        for (String id : ids) {
            Material material = config.getMaterial(path + ".items." + id + ".item");
            ItemStack item = new ItemStack(material);
            int meta = config.getConfig().getInt(path + ".items." + id + ".meta");
            String itemName = config.getConfig().getString(path + ".items." + id + ".name");
            List<String> commands = config.getConfig().getStringList(path + ".items." + id + ".commands");
            int percentage = config.getConfig().getInt(path + ".items." + id + ".percentage");
            int tier = config.getConfig().getInt(path + ".items." + id + ".tier");
            crateItems.add(new CrateItem(id, item, meta, itemName, commands, percentage, CrateTier.valueOf("TIER" + tier)));
        }
        return new Crate(name, crateItems);
    }

    public List<CrateItem> getCrateItems() {
        return crateItems;
    }

    public String getName() {
        return name;
    }
}
