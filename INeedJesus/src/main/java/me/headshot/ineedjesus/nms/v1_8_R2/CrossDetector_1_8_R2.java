package me.headshot.ineedjesus.nms.v1_8_R2;

import me.headshot.ineedjesus.nms.Cross;
import me.headshot.ineedjesus.nms.CrossDetector;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class CrossDetector_1_8_R2 implements CrossDetector {
    @Override
    public boolean isMatching(Block block) {
        return getDetectedBlocks(block) != null;
    }

    @Override
    public void setCross(Block block) {
        World nmsWorld = ((CraftWorld)block.getWorld()).getHandle();
        List<Location> blockLocations = new ArrayList<>();
        for (int k = 0; k < getDetectorCrossLong(block.getType().name().toLowerCase()).c(); ++k) {
            for (int l = 0; l < this.getDetectorCrossLong(block.getType().name().toLowerCase()).b(); ++l) {
                BlockPosition pos = getDetectedBlocks(block).a(k, l, 0).d();
                Location location = new Location(block.getWorld(), pos.getX(), pos.getY(), pos.getZ());
                blockLocations.add(location);
            }
        }
        ArmorStand armorStand = (ArmorStand) block.getWorld().spawnEntity(block.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setCustomName(ChatColor.DARK_RED + "Mighty Cross");
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        Cross cross = new Cross_1_8_R2(blockLocations, armorStand);
    }

    private ShapeDetector.ShapeDetectorCollection getDetectedBlocks(Block block){
        World world = ((CraftWorld)block.getWorld()).getHandle();
        BlockPosition blockPos = new BlockPosition(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
        String mat = block.getType().name().toLowerCase();
        return getDetectorCrossLong(mat).a(world, blockPos);
    }

    private ShapeDetector getDetectorCrossLong(String mat) {
        return ShapeDetectorBuilder.a().a(new String[]{"~#~", "###", "~#~", "~#~"}).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(net.minecraft.server.v1_8_R2.Block.getByName(mat)))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
    }
}
