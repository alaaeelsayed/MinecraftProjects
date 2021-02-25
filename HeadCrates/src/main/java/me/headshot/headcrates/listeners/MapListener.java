package me.headshot.headcrates.listeners;

import me.headshot.headcrates.CrateRoller;
import me.headshot.headcrates.render.AnimationRenderer;
import me.headshot.headcrates.util.CrateUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class MapListener implements Listener {

    @EventHandler
    public void onInit(MapInitializeEvent event) {
        //TODO: not cancellable
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (CrateUtil.isUsing(player)) {
            AnimationRenderer renderer = CrateUtil.getRenderer(player);
            if (renderer.laterTask != null)
                renderer.laterTask.cancel();
            Bukkit.getScheduler().cancelTask(renderer.taskID);
            for (ItemStack item : player.getInventory().getContents())
                if (item != null && item.getType() == Material.MAP && item.getDurability() == renderer.latestView.getId())
                    player.getInventory().remove(item);
            renderer.runnable.run(renderer.wonItem);
            CrateUtil.removeUsing(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if (!event.hasItem()) return;
        ItemStack item = event.getItem();
        if (!CrateUtil.isCrate(item)) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        player.updateInventory();
        if (CrateUtil.isUsing(player)) {
            player.sendMessage(ChatColor.DARK_RED + "You're already using a crate, please wait!");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        String crateName = ChatColor.stripColor(meta.getDisplayName().toLowerCase()).split(" ")[0];
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have enough space in your inventory.");
            return;
        }
        MapView mapView = Bukkit.createMap(player.getWorld());
        CrateRoller.roll(mapView, (wonItem) -> {
            player.sendMessage(ChatColor.GREEN + "You've won " + ChatColor.DARK_GREEN + wonItem.getName());
            for (String command : wonItem.getCommands())
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
        }, CrateRoller.getCrate(crateName));
        ItemStack iStack = new ItemStack(Material.MAP, 1);
        iStack.setDurability(mapView.getId());

        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(ChatColor.RED + "Rolling " + StringUtils.capitalize(crateName) + " Crate");
        iStack.setItemMeta(iMeta);
        if (item.getAmount() > 1)
            item.setAmount(item.getAmount() - 1);
        else
            player.getInventory().removeItem(item);
        player.getInventory().addItem(iStack);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (!CrateUtil.isRollingCrate(item)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (ItemStack item : event.getDrops()) {
            if (item == null) continue;
            if (CrateUtil.isRollingCrate(item))
                event.getDrops().remove(event.getDrops().indexOf(item));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        ItemStack item2 = event.getCursor();
        if (item == null) return;
        if (CrateUtil.isRollingCrate(item) || CrateUtil.isRollingCrate(item2)) event.setCancelled(true);
    }

}
