package me.headshot.ineedjesus.nms;

import org.bukkit.block.Block;

public interface CrossDetector {
    public boolean isMatching(Block block);
    public void setCross(Block block);
}
