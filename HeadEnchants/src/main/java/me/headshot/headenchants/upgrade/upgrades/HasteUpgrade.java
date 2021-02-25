package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.GearType;
import me.headshot.headenchants.utils.type.ItemType;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HasteUpgrade extends Upgrade {

	public HasteUpgrade(int level) {
		super("haste", level, 3, HeadEnchants.getConfigManager().getLore("haste"), (ItemType[]) ArrayUtils.addAll(GearType.TOOL.getItemTypes(), GearType.WEAPON.getItemTypes()));

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Haste %level%";
	}

	@Override
	public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
	}

	@Override
	public void onSwitch(PlayerItemHeldEvent event, boolean off) {
		if (!off) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, getLevel() - 1));
		} else {
			event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
		}
	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, Player player) {
	}

	@Override
	public void onEvent(Event event,Player player) {
		if(event instanceof PlayerDropItemEvent){
			PlayerDropItemEvent playerDropItemEvent = (PlayerDropItemEvent) event;
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
		}
	}

}
