package me.headshot.ineedjesus.listeners;

import me.headshot.ineedjesus.nms.Cross;
import me.headshot.ineedjesus.nms.CrossDetector;
import me.headshot.ineedjesus.util.CrossUtil;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class CrossListener implements Listener {
    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        CrossDetector crossDetector = CrossUtil.getCrossDetector();
        List<Cross> crosses = CrossUtil.getCrosses();
        Block placedBlock = event.getBlockPlaced();
        if (crosses.stream().anyMatch(cross -> cross.hasBlock(placedBlock))) {
            event.setCancelled(true);
            return;
        }
        if (crossDetector.isMatching(placedBlock)) {
            crossDetector.setCross(placedBlock);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        List<Cross> crosses = CrossUtil.getCrosses();
        Block brokenBlock = event.getBlock();
        CrossDetector crossDetector = CrossUtil.getCrossDetector();
        if (crosses.stream().anyMatch(cross -> cross.hasBlock(brokenBlock)))
            crosses.stream().filter(cross -> cross.hasBlock(brokenBlock)).forEach(Cross::destroy);
    }
}
