package me.headshot.headtop;

import me.headshot.headtop.commands.FTopCommand;
import me.headshot.headtop.commands.RecalcCommand;
import me.headshot.headtop.config.HeadTopConfig;
import me.headshot.headtop.config.HeadTopLang;
import me.headshot.headtop.handler.PlacedBlockHandler;
import me.headshot.headtop.handler.PlacedSpawnerHandler;
import me.headshot.headtop.listeners.CommandListener;
import me.headshot.headtop.listeners.PlacedBlockListener;
import me.headshot.headtop.listeners.PlacedSpawnerListener;
import me.headshot.headtop.util.FTopUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class HeadTop extends JavaPlugin {
    private static HeadTop instance;
    private static Economy econ;

    public void onEnable(){
        instance = this;
        PlacedBlockHandler.init();
        PlacedSpawnerHandler.init();
        saveDefaultConfig();
        HeadTopConfig.load(getConfig());
        HeadTopConfig.save(getConfig());
        saveConfig();
        reloadLangFile();
        if (!setupEconomy() ) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("ftop").setExecutor(new FTopCommand());
        getCommand("recalc").setExecutor(new RecalcCommand());
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlacedBlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlacedSpawnerListener(), this);
        FTopUtil.loadTopFactions(null, 100);
    }

    public void onDisable(){
        PlacedBlockHandler.startSaveThread();
        PlacedSpawnerHandler.startSaveThread();
    }

    public void reloadLangFile() {
        File lang = new File(getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                this.setEnabled(false);
            }
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for (HeadTopLang langItem : HeadTopLang.values()) {
            if (conf.getString(langItem.getPath()) == null) {
                conf.set(langItem.getPath(), langItem.getDefault());
            }
        }
        HeadTopLang.setFile(conf);
        try {
            conf.save(lang);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HeadTop get(){
        return instance;
    }

    public static Economy getEconomy(){
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
