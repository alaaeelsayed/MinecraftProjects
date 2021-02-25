package me.headshot.headtop.struct;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PlacedBlock extends StoredPlaceable {
    private Material blockType;

    public PlacedBlock(int x, int y, int z, String worldName, long placeTime, Material blockType) {
        super(x, y, z, worldName, placeTime);
        this.blockType = blockType;
    }

    public PlacedBlock(Block block, long placeTime) {
        this(block.getX(), block.getY(), block.getZ(), block.getWorld().getName(), placeTime, block.getType());
    }

    public Material getBlockType() {
        return this.blockType;
    }

    @Override
    public boolean equals(Object object){
        if (object == null) { return false; }
        if (object == this) { return true; }
        if (object.getClass() != getClass()) {
            return false;
        }
        PlacedBlock other = (PlacedBlock) object;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(blockType, other.blockType)
                .isEquals();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(13, 39)
                .appendSuper(super.hashCode())
                .append(blockType)
                .toHashCode();
    }

    @Override
    public String toString(){
        return super.toString() + "," + blockType.name();
    }

}
