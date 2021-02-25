package me.headshot.headtop.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum HeadTopLang {
    PLAYER_ONLY("player-only", "&4You can only execute this command as a player."),
    NO_PERMISSION("no-permission", "&3You don't have permission to run this command."),
    CALCULATING("calculating", "&4&lThe FTop is currently being recalculated...");

    private String path;
    private String defaultVal;
    private static YamlConfiguration LANG;

    HeadTopLang(String path, String defaultVal) {
        this.path = path;
        this.defaultVal = defaultVal;
    }

    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, defaultVal));
    }

    public String format(Object... args) {
        return String.format(toString(), args);
    }

    public String getDefault() {
        return defaultVal;
    }

    public String getPath() {
        return path;
    }
}
