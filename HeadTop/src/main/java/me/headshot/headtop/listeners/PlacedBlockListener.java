package me.headshot.headtop.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import me.headshot.headtop.HeadTop;
import me.headshot.headtop.handler.PlacedBlockHandler;
import me.headshot.headtop.struct.PlacedBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class PlacedBlockListener implements Listener {
    private HashSet<String> scannedChunks = new HashSet<>();

    @EventHandler(priority= EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!PlacedBlockHandler.getTypesToTrack().contains(event.getBlock().getType())) return;
        PlacedBlockHandler.getTrackedPlacedBlocks().put(toPlacedBlockString(event.getBlock()), toPlacedBlock(event.getBlock()));
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!PlacedBlockHandler.getTypesToTrack().contains(event.getBlock().getType())) return;
        if (!PlacedBlockHandler.getTrackedPlacedBlocks().containsKey(toPlacedBlockString(event.getBlock()))) return;
        PlacedBlockHandler.getTrackedPlacedBlocks().remove(toPlacedBlockString(event.getBlock()));
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        ArrayList<Location> blocksToCheck = new ArrayList<>();
        for(Block block : event.blockList()){
            if(!PlacedBlockHandler.getTypesToTrack().contains(block.getType())) continue;
            blocksToCheck.add(block.getLocation());
        }
        Bukkit.getScheduler().runTaskLater(HeadTop.get(), () -> {
            for(Location blockToCheck : blocksToCheck){
                if (PlacedBlockHandler.getTypesToTrack().contains(blockToCheck.getBlock().getType())) continue;
                String s = toPlacedBlockString(blockToCheck.getBlock());
                PlacedBlockHandler.getTrackedPlacedBlocks().remove(s);
            }
        }, 5L);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onChunkLoad(ChunkLoadEvent event) {
        String cs = event.getChunk().getX() + "," + event.getChunk().getZ();
        if (this.scannedChunks.contains(cs)) {
            return;
        }
        FLocation fLocation = new FLocation(event.getChunk().getBlock(0, 0, 0));
        if (Board.getInstance().getFactionAt(fLocation).isNormal()) {
            for (BlockState blockState : event.getChunk().getTileEntities()) {
                Block block = blockState.getBlock();
                String blockString = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
                if (!PlacedBlockHandler.getTypesToTrack().contains(blockState.getBlock().getType()) || PlacedBlockHandler.isTracked(block))
                    continue;
                PlacedBlockHandler.getTrackedPlacedBlocks().put(blockString, new PlacedBlock(block, System.currentTimeMillis()));
            }
        }
        this.scannedChunks.add(cs);
    }

    private String toPlacedBlockString(Block block){
        return block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ();
    }

    private PlacedBlock toPlacedBlock(Block block){
        return new PlacedBlock(block, System.currentTimeMillis());
    }

}
