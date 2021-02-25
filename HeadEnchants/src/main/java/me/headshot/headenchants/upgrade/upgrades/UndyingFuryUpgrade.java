package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.GearType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import me.headshot.headenchants.events.ArmorEquipEvent;

import java.util.*;

public class UndyingFuryUpgrade extends Upgrade {

	private static Set<UUID> undying = new HashSet<>();

	public static void setUndying(Player player, boolean set){
		if(set)
			undying.add(player.getUniqueId());
		else
			undying.remove(player.getUniqueId());
	}

	public static boolean isUndying(Player player){
return undying.contains(player.getUniqueId());
	}

	public UndyingFuryUpgrade(int level) {
		super("undying-fury", level, 2, HeadEnchants.getConfigManager().getLore("undying-fury"), GearType.ARMOR.getItemTypes());

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Undying Fury %level%";
	}

	@Override
	public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
	}

	@Override
	public void onSwitch(PlayerItemHeldEvent event, boolean off) {

	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, Player player) {
		if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
		if(player == event.getDamager()) return;

		double damage = event.getDamage();
		if(isUndying(player)){
			if(player.getHealth() <= damage)
				event.setDamage(0);
		}
		if (player.getHealth()-damage < 4) {
			if(!HeadEnchants.getUpgradeManager()
					.getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level)))
				return;
			Bukkit.getScheduler().runTaskLater(HeadEnchants.get(), () -> setUndying(player, false), level == 1 ? 5*20 : 7*20);
			setUndying(player, true);
			player.sendMessage(HeadEnchants.getConfigManager().getMessage("activation")
					.replaceAll("%upgrade%", "Undying Fury"));
		}
	}

	@Override
	public void onEvent(Event event,Player player) {
	}

}
