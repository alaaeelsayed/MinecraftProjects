package me.headshot.ineedjesus.commands;

import me.headshot.ineedjesus.util.JesusUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetJesusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Player only command.");
            return true;
        }
        Player player = (Player) sender;
        if (JesusUtil.getJesus() == null) {
            JesusUtil.spawnJesus(player.getLocation());
            return true;
        }
        JesusUtil.getJesus().setLocation(player.getLocation());
        return true;
    }
}
