package me.headshot.headenchants.upgrade;

import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.headshot.headenchants.events.ArmorEquipEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Upgrade {
	protected final String id;
	protected int level;
	protected final int maxLevel;
	protected final ItemType[] types;
	protected String lore;

	public Upgrade(final String id, int level, final int maxLevel, final String lore, final ItemType... types) {
		this.id = id;
		this.level = level;
		this.maxLevel = maxLevel;
		this.lore = lore;
		this.types = types;
	}

	public abstract String getDefaultLore();

	public abstract void onWear(ArmorEquipEvent event, Player player, boolean removed);

	public abstract void onSwitch(PlayerItemHeldEvent event, boolean off);

	public abstract void onDamage(EntityDamageByEntityEvent event, Player player);

	public abstract void onEvent(Event event, Player player);

	public String getID() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMaximumLevel() {
		return maxLevel;
	}

	public ItemType[] getTypes() {
		return types;
	}

	public String getLore() {
		return lore.replaceAll("%level%", getLevelString());
	}

	public boolean isOfType(ItemStack item) {
		for (ItemType type : types)
			if (ItemType.fromItem(item) == type)
				return true;
		return false;
	}

	protected String getLevelString() {
		switch (level) {
		case 1:
			return "I";
		case 2:
			return "II";
		case 3:
			return "III";
		default:
			return "I";
		}
	}

}
