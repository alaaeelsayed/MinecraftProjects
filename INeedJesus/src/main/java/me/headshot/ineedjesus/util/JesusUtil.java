package me.headshot.ineedjesus.util;

import me.headshot.ineedjesus.INeedJesus;
import me.headshot.ineedjesus.nms.Jesus;
import me.headshot.ineedjesus.tasks.TalkRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public class JesusUtil {
    private static Class<? extends Jesus> jesusClass;
    private static Jesus jesus = null;

    public static void setJesusClass(Class<? extends Jesus> jesusClass) {
        JesusUtil.jesusClass = jesusClass;
    }

    public static void spawnJesus(Location location) {
        try {
            jesus = jesusClass.getConstructor(World.class).newInstance(location.getWorld());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        jesus.setLocation(location);
        jesus.spawn();
    }

    public static Jesus getJesus() {
        return jesus;
    }

    public static void sayRandomVerse(INeedJesus plugin) {
        List<String> phrases = plugin.getConfig().getStringList("verses");
        String phrase = phrases.get(new Random().nextInt(phrases.size()));
        Bukkit.broadcastMessage(ChatColor.BOLD + "Jesus " + ChatColor.DARK_GRAY + "» " + ChatColor.RED + phrase);
    }
}
