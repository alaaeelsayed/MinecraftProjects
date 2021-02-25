package me.headshot.headtop.struct;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class PlacedSpawner extends StoredPlaceable {
    private EntityType spawnerType;

    public PlacedSpawner(Block block, long placeTime) {
        this(block.getX(), block.getY(), block.getZ(), block.getWorld().getName(), placeTime, ((CreatureSpawner)block.getState()).getSpawnedType());
    }

    public PlacedSpawner(int x, int y, int z, String worldName, long placeTime, EntityType spawnerType) {
        super(x, y, z, worldName, placeTime);
        this.spawnerType = spawnerType;
    }

    public EntityType getSpawnerType() {
        return this.spawnerType;
    }

    @Override
    public boolean equals(Object object){
        if (object == null) { return false; }
        if (object == this) { return true; }
        if (object.getClass() != getClass()) {
            return false;
        }
        PlacedSpawner other = (PlacedSpawner) object;
        return new EqualsBuilder()
                .appendSuper(super.equals(other))
                .append(spawnerType, other.spawnerType)
                .isEquals();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(19, 33)
                .appendSuper(super.hashCode())
                .append(spawnerType)
                .toHashCode();
    }

    @Override
    public String toString(){
        return super.toString() + "," + spawnerType.name();
    }

}
