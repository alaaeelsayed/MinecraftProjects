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

public class EnderMasteryUpgrade extends Upgrade {

	public EnderMasteryUpgrade(int level) {
		super("ender-mastery", level, 3, HeadEnchants.getConfigManager().getLore("ender-mastery"), GearType.ARMOR.getItemTypes());

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Ender Mastery %level%";
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
		if(!HeadEnchants.getUpgradeManager()
				.getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level)))
			return;

		for(ItemStack armorPiece : player.getInventory().getArmorContents()){
			short newDurability = (short) (armorPiece.getDurability() + 10);
			armorPiece.setDurability((short)Math.max(newDurability, armorPiece.getDurability()));
		}
		player.sendMessage(HeadEnchants.getConfigManager().getMessage("activation")
				.replaceAll("%upgrade%", "Reforge"));
	}

	@Override
	public void onEvent(Event event,Player player) {
	}

}
