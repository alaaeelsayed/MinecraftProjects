package me.headshot.headtop.struct;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class StoredPlaceable {
    private int x;
    private int y;
    private int z;
    private int chunkX;
    private int chunkZ;
    private String worldName;
    private long placeTime;

    public StoredPlaceable(int x, int y, int z, String worldName, long placeTime){
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = x >> 4;
        this.chunkZ = z >> 4;
        this.worldName = worldName;
        this.placeTime = placeTime;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public Location getLocation(){
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    public long getTimePlaced(){
        return System.currentTimeMillis() - placeTime;
    }

    public long getPlaceTime(){
        return placeTime;
    }

    @Override
    public boolean equals(Object object){
        if (object == null) { return false; }
        if (object == this) { return true; }
        if (object.getClass() != getClass()) {
            return false;
        }
        StoredPlaceable other = (StoredPlaceable) object;
        return new EqualsBuilder()
                .append(x, other.x)
                .append(y, other.y)
                .append(z, other.z)
                .append(worldName, other.worldName)
                .isEquals();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(17, 37).
                append(x).
                append(y).
                append(z).
                append(worldName).
                toHashCode();
    }

    @Override
    public String toString(){
        return worldName + "," + x + "," + y + "," + z + "," + placeTime;
    }
}