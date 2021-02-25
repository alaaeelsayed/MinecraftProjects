package me.headshot.ineedjesus.nms.v1_8_R2;

import me.headshot.ineedjesus.nms.Cross;
import me.headshot.ineedjesus.util.CrossUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

public class Cross_1_8_R2 implements Cross {
    private List<Location> crossLocations = new ArrayList<>();
    private final ArmorStand armorStand;

    Cross_1_8_R2(List<Location> locations, ArmorStand armorStand) {
        this.armorStand = armorStand;
        this.crossLocations = locations;
    }

    @Override
    public void destroy() {
        armorStand.remove();
        crossLocations.clear();
        CrossUtil.removeCross(this);
    }

    @Override
    public boolean hasBlock(Block block) {
        return crossLocations.stream().anyMatch(location -> location.equals(block.getLocation()));
    }
}
