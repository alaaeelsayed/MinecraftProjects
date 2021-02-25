package me.headshot.headenchants.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.headshot.headenchants.listeners.gui.GUIManager;

public class EnchanterCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			GUIManager.displayUpgrades(player);
		}else
			sender.sendMessage("You need to be a player to use this command.");
		return false;
	}
}
