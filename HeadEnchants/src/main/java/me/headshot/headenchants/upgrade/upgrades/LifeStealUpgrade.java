package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.headshot.headenchants.events.ArmorEquipEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class LifeStealUpgrade extends Upgrade {

	public LifeStealUpgrade(int level) {
		super("lifesteal", level, 3, HeadEnchants.getConfigManager().getLore("lifesteal"), ItemType.SWORD, ItemType.AXE);

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Lifesteal %level%";
	}

	@Override
	public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
	}

	@Override
	public void onSwitch(PlayerItemHeldEvent event, boolean off) {

	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, Player player) {
		if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		
		if(!HeadEnchants.getUpgradeManager().getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level))) return;
		
		Player damager = (Player) event.getDamager();
		
		final double newHealth = damager.getHealth() + 2D;
		
		if(newHealth > damager.getMaxHealth()) damager.setHealth(damager.getMaxHealth());
		else damager.setHealth(newHealth);
		
		damager.sendMessage(HeadEnchants.getConfigManager().getMessage("activation")
				.replaceAll("%upgrade%", "Lifesteal"));
		
	}

	@Override
	public void onEvent(Event event, Player player) {
	}
	
}
