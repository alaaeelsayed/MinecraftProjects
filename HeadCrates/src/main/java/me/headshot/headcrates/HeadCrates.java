package me.headshot.headcrates;

import me.headshot.headcrates.command.HeadCratesCommand;
import me.headshot.headcrates.config.HeadCratesConfig;
import me.headshot.headcrates.listeners.MapListener;
import me.headshot.headcrates.util.CrateUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
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
public class HeadCrates extends JavaPlugin {

    private static HeadCrates instance;

    private HeadCratesConfig cratesConfig;

    public Font mcFont;

    public void onEnable() {
        try {
            File tempFile = File.createTempFile("temp", "font");
            tempFile.deleteOnExit();
            FileUtils.copyInputStreamToFile(getResource("fonts/minecraft.ttf"), tempFile);
            mcFont = Font.createFont(Font.TRUETYPE_FONT,
                    tempFile);
        } catch (IOException | FontFormatException e) {
        }
        instance = this;
        saveDefaultConfig();
        this.cratesConfig = new HeadCratesConfig("crates");
        CrateUtil.initCrates(cratesConfig);
        getCommand("iratuscrates").setExecutor(new HeadCratesCommand());
        getServer().getPluginManager().registerEvents(new MapListener(), this);
    }

    public HeadCratesConfig getCratesConfig() {
        return cratesConfig;
    }

    public static HeadCrates getInstance() {
        return instance;
    }
}
