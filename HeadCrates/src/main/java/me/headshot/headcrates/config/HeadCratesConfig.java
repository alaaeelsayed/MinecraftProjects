package me.headshot.headcrates.config;

import me.headshot.headcrates.HeadCrates;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class HeadCratesConfig {
    private final File file;
    private YamlConfiguration config;

    public HeadCratesConfig(String fileName) {
        this.file = new File(HeadCrates.getInstance().getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            HeadCrates.getInstance().saveResource(fileName + ".yml", false);
        }
        reload();
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public Material getMaterial(String path){
        return Material.valueOf(config.getString(path).toUpperCase());
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
