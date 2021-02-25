package me.headshot.ineedjesus.nms;

import me.headshot.ineedjesus.INeedJesus;
import org.bukkit.Location;

public interface Jesus {
    public void sendRandomMessageWithVoice(INeedJesus plugin);

    public Location getLocation();

    public void setLocation(Location loc);

    public void move(Location location);

    public String getCustomName();

    public void setCustomName(String name);

    public void spawn();

}
