package me.headshot.headtop.commands;

import me.headshot.headtop.HeadTop;
import me.headshot.headtop.config.HeadTopConfig;
import me.headshot.headtop.config.HeadTopLang;
import me.headshot.headtop.util.FTopUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RecalcCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("ftop.recalc")){
            sender.sendMessage(HeadTopLang.NO_PERMISSION.format());
            return true;
        }
        FTopUtil.loadTopFactions(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(HeadTop.get(), () -> {
                    if (!(sender instanceof Player)) return;
                    Player pl = (Player)sender;
                    pl.performCommand("ftop");
                }
        ), -1);
        return true;
    }
}
