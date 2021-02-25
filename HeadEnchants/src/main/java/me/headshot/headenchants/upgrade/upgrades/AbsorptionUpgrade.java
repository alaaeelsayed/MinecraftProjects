package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbsorptionUpgrade extends Upgrade {

	public AbsorptionUpgrade(int level) {
		super("absorption", level, 3, HeadEnchants.getConfigManager().getLore("absorption"), ItemType.HELMET);

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Absorption %level%";
	}

	@Override
	public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
		if (!removed) {
			if(!HeadEnchants.get().getCombatTagPlus().getTagManager().isTagged(player.getUniqueId()))
				player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, getLevel() - 1));
		} else {
			player.removePotionEffect(PotionEffectType.ABSORPTION);
		}
	}

	@Override
	public void onSwitch(PlayerItemHeldEvent event, boolean off) {

	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, Player player) {
	}

	@Override
	public void onEvent(Event event,Player player) {
	}

}
