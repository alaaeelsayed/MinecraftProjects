package me.headshot.ineedjesus;

import me.headshot.ineedjesus.commands.GetJesusCommand;
import me.headshot.ineedjesus.listeners.CrossListener;
import me.headshot.ineedjesus.nms.v1_8_R1.CrossDetector_1_8_R1;
import me.headshot.ineedjesus.nms.v1_8_R1.Jesus_1_8_R1;
import me.headshot.ineedjesus.nms.v1_8_R2.CrossDetector_1_8_R2;
import me.headshot.ineedjesus.nms.v1_8_R2.Jesus_1_8_R2;
import me.headshot.ineedjesus.nms.v1_8_R3.CrossDetector_1_8_R3;
import me.headshot.ineedjesus.nms.v1_8_R3.JesusLover_1_8_R3;
import me.headshot.ineedjesus.nms.v1_8_R3.Jesus_1_8_R3;
import me.headshot.ineedjesus.tasks.TalkRunnable;
import me.headshot.ineedjesus.util.CrossUtil;
import me.headshot.ineedjesus.util.JesusUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class INeedJesus extends JavaPlugin {
    private static INeedJesus instance;
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        switch (version) {
            case "v1_8_R1":
                JesusUtil.setJesusClass(Jesus_1_8_R1.class);
                Jesus_1_8_R1.addToMaps();
                CrossUtil.setCrossDetector(new CrossDetector_1_8_R1());
                break;
            case "v1_8_R2":
                JesusUtil.setJesusClass(Jesus_1_8_R2.class);
                Jesus_1_8_R2.addToMaps();
                CrossUtil.setCrossDetector(new CrossDetector_1_8_R2());
                break;
            case "v1_8_R3":
                JesusUtil.setJesusClass(Jesus_1_8_R3.class);
                Jesus_1_8_R3.addToMaps();
                CrossUtil.setCrossDetector(new CrossDetector_1_8_R3());
                JesusLover_1_8_R3.addToMaps();
                break;
            default:
                getLogger().severe("You are using version: " + version + " which is not supported by this plugin, disabling.");
                getServer().getPluginManager().disablePlugin(this);
                break;
        }
        getCommand("getjesus").setExecutor(new GetJesusCommand());
        getServer().getPluginManager().registerEvents(new CrossListener(), this);
        getServer().getScheduler().runTaskTimer(this, new TalkRunnable(this), 0, 20 * 60);
    }

    public static INeedJesus hook(){
        return instance;
    }

}