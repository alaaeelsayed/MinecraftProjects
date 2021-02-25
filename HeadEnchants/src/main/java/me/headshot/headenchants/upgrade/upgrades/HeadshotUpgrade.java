package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class HeadshotUpgrade extends Upgrade {

	public HeadshotUpgrade(int level) {
		super("headshot", level, 1, HeadEnchants.getConfigManager().getLore("headshot"), ItemType.BOW);

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Headshot %level%";
	}

	@Override
	public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
	}

	@Override
	public void onSwitch(PlayerItemHeldEvent event, boolean off) {

	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, Player player) {
		if(!(event.getDamager() instanceof Arrow)) return;

		Arrow a = (Arrow) event.getDamager();
		if(a.getShooter() != player) return;

		if(!HeadEnchants.getUpgradeManager().getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level))) return;

		event.setDamage(event.getDamage()*2);

		player.sendMessage(HeadEnchants.getConfigManager().getMessage("activation")
				.replaceAll("%upgrade%", "Headshot"));
	}

	@Override
	public void onEvent(Event event,Player player) {
	}

}
