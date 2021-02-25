package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class GrindUpgrade extends Upgrade {

	public GrindUpgrade(int level) {
		super("grind", level, 3, HeadEnchants.getConfigManager().getLore("grind"), ItemType.SWORD, ItemType.AXE);

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Grind %level%";
	}

	@Override
	public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
	}

	@Override
	public void onSwitch(PlayerItemHeldEvent event, boolean off) {
	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, Player player) {
	}

	@Override
	public void onEvent(Event event,Player player) {
		if (event instanceof EntityDeathEvent) {
			EntityDeathEvent deathEvent = ((EntityDeathEvent) event);
			if (deathEvent.getEntity() instanceof Player) return;

			double multiplier = 1.5;
			if (getLevel() == 2)
				multiplier = 2;
			else if(getLevel() == 3)
				multiplier = 2.5;

			deathEvent.setDroppedExp((int) (deathEvent.getDroppedExp() * multiplier));
		}
	}

}
