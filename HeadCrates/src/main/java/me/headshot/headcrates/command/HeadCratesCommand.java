package me.headshot.headcrates.command;

import me.headshot.headcrates.CrateRoller;
import me.headshot.headcrates.crate.Crate;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class HeadCratesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("headcrates.give")) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command!");
            return true;
        }
        if(args.length < 1){
            sender.sendMessage(ChatColor.GOLD + "------ Head Crates Help ------");
            sender.sendMessage(ChatColor.GOLD + "/hcrates give [player] [crate] [amount] - Gives a certain player a certain crate.");
            sender.sendMessage(ChatColor.GOLD + "/hcrates giveall [crate] [amount] - Gives all the players a certain crate.");
            sender.sendMessage(ChatColor.GOLD + "/hcrates list - Lists all the available crates.");
            return true;
        }
        String instruction = args[0];
        switch (instruction.toLowerCase()) {
            case "give":
                if (args.length < 4)
                    sender.sendMessage(ChatColor.DARK_RED + "Incorrect syntax, " + ChatColor.DARK_GRAY + "/hcrates give [player] [crate] [amount]");
                else {
                    String playerName = args[1];
                    List<Player> players = Bukkit.matchPlayer(playerName);
                    if (players.size() == 0) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid player: " + playerName);
                        return true;
                    }
                    Player player = players.get(0);
                    Crate crate = CrateRoller.getCrate(args[2].toLowerCase());
                    if (crate == null) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid crate: " + args[2]);
                        return true;
                    }
                    int amount;
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException exception) {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid amount: " + args[3]);
                        return true;
                    }

                    ItemStack iStack = new ItemStack(Material.EMPTY_MAP);

                    iStack.setAmount(amount);

                    ItemMeta iMeta = iStack.getItemMeta();
                    iMeta.setDisplayName(ChatColor.RED + StringUtils.capitalize(crate.getName()) + " Crate");

                    iMeta.setLore(Collections.singletonList(ChatColor.GRAY + "An incredible crate forged with godly power!"));

                    iStack.setItemMeta(iMeta);

                    HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(iStack);

                    for (Map.Entry<Integer, ItemStack> item : leftOver.entrySet()) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item.getValue());
                    }
                }
                return true;
            case "giveall":

                if (args.length < 3) {
                    sender.sendMessage(ChatColor.DARK_RED + "Incorrect syntax, " + ChatColor.DARK_GRAY + "/hcrates giveall [crate] [amount]");
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Crate crate = CrateRoller.getCrate(args[1]);
                        if (crate == null) {
                            sender.sendMessage(ChatColor.DARK_RED + "Invalid crate: " + args[1]);
                            return false;
                        }
                        int amount = 0;
                        try {
                            amount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(ChatColor.DARK_RED + "Invalid amount: " + args[2]);
                            return false;
                        }
                        ItemStack iStack = new ItemStack(Material.EMPTY_MAP, amount);

                        ItemMeta iMeta = iStack.getItemMeta();
                        iMeta.setDisplayName(ChatColor.RED + StringUtils.capitalize(crate.getName()) + " Crate");

                        iMeta.setLore(Collections.singletonList(ChatColor.GRAY + "An incredible crate forged with godly power!"));

                        iStack.setItemMeta(iMeta);

                        HashMap<Integer, ItemStack> leftOver = player.getInventory().addItem(iStack);

                        for (Map.Entry<Integer, ItemStack> item : leftOver.entrySet()) {
                            player.getWorld().dropItemNaturally(player.getLocation(), item.getValue());
                        }
                    }
                }
                return true;
            case "list":
                sender.sendMessage(ChatColor.RED + "Available crates: " + CrateRoller.getCrates().keySet().toString());
                return true;
            case "help":
            default:
                sender.sendMessage(ChatColor.GOLD + "------ Iratus Crates Help ------");
                sender.sendMessage(ChatColor.GOLD + "/hcrates give [player] [crate] [amount] - Gives a certain player a certain crate.");
                sender.sendMessage(ChatColor.GOLD + "/hcrates giveall [crate] [amount] - Gives all the players a certain crate.");
                sender.sendMessage(ChatColor.GOLD + "/hcrates list - Lists all the available crates.");
                return true;
        }
    }
}
