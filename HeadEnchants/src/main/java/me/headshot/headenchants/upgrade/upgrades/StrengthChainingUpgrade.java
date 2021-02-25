package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.GearType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class StrengthChainingUpgrade extends Upgrade {

	public StrengthChainingUpgrade(int level) {
		super("strength-chaining", level, 1, HeadEnchants.getConfigManager().getLore("strength-chaining"), GearType.ARMOR.getItemTypes());

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Strengh Chaining %level%";
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

		Player damaged = (Player) event.getEntity();

		if(!HeadEnchants.getUpgradeManager().getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level))) return;
		if(player == damaged) return;

		int amount = 0;
		for(ItemStack item : player.getInventory().getArmorContents()){
			for(Upgrade upgrade : HeadEnchants.getUpgradeManager().getUpgrades(item)){
				if(upgrade.getID().equals(getID())){
					amount++;
					break;
				}
			}
		}

		if(amount < 4) return;

		if(!HeadEnchants.getUpgradeManager().getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level))) return;

		boolean flag = player.getFallDistance() > 0.0F && !player.isOnGround() && !player.hasPotionEffect(PotionEffectType.BLINDNESS) && player.getVehicle() == null;

		if(!flag) return;

		event.setDamage(event.getDamage() + (int) (event.getDamage() * 0.05D));

		player.sendMessage(HeadEnchants.getConfigManager().getMessage("activation")
				.replaceAll("%upgrade%", "Strength Chaining"));

	}

	@Override
	public void onEvent(Event event,Player player) {
	}

}
