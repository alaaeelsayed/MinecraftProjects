package me.headshot.headtop.listeners;

import me.headshot.headtop.HeadTop;
import me.headshot.headtop.handler.PlacedSpawnerHandler;
import me.headshot.headtop.struct.PlacedSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;

public class PlacedSpawnerListener implements Listener {

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() != Material.MOB_SPAWNER) return;
        PlacedSpawnerHandler.getPlacedSpawners().put(toPlacedSpawnerString(e.getBlock()), toPlacedSpawner(e.getBlock()));
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.MOB_SPAWNER) return;
        if (!PlacedSpawnerHandler.getPlacedSpawners().containsKey(toPlacedSpawnerString(e.getBlock()))) return;
        PlacedSpawnerHandler.getPlacedSpawners().remove(toPlacedSpawnerString(e.getBlock()));
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent e) {
        ArrayList<Location> blocksToCheck = new ArrayList<>();
        for(Block block : e.blockList()){
            if(block.getType() != Material.MOB_SPAWNER) continue;
            blocksToCheck.add(block.getLocation());
        }
        Bukkit.getScheduler().runTaskLater(HeadTop.get(), () -> {
            for(Location blockToCheck : blocksToCheck){
                if (blockToCheck.getBlock().getType() == Material.MOB_SPAWNER) continue;
                String s = toPlacedSpawnerString(blockToCheck.getBlock());
                PlacedSpawnerHandler.getPlacedSpawners().remove(s);
            }
        }, 5L);
    }

    private String toPlacedSpawnerString(Block block){
        return block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
    }

    private PlacedSpawner toPlacedSpawner(Block block){
        return new PlacedSpawner(block, System.currentTimeMillis());
    }

}
