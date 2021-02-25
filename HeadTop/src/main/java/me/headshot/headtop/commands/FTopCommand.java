package me.headshot.headtop.commands;

import me.headshot.headtop.config.HeadTopConfig;
import me.headshot.headtop.config.HeadTopLang;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FTopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(HeadTopLang.PLAYER_ONLY.format());
            return true;
        }
        Player player = (Player) sender;
        int page = 1;
        if (args.length == 1 && StringUtils.isNumeric(args[0])) {
            page = Integer.parseInt(args[0]);
        }

        HeadTopConfig.sendFTopMessage(player, page);
        return true;
    }
}
