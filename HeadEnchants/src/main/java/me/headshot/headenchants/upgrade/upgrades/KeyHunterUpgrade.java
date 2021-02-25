package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.GearType;
import me.headshot.headenchants.utils.type.ItemType;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class KeyHunterUpgrade extends Upgrade {

	public KeyHunterUpgrade(int level) {
		super("key-hunter", level, 3, HeadEnchants.getConfigManager().getLore("key-hunter"), (ItemType[]) ArrayUtils.add(GearType.TOOL.getItemTypes(), ItemType.SWORD));

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Key Hunter %level%";
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
		if(event instanceof BlockBreakEvent){
			if(((BlockBreakEvent) event).isCancelled()) return;
			BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;

			if(!HeadEnchants.getUpgradeManager().getRandomPercentage(HeadEnchants.getConfigManager().getPercentage(id, level))) return;

			String chosenCommand = HeadEnchants.getUpgradeManager().getRandomCommand(HeadEnchants.getConfigManager().getCommands(id, level));

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), chosenCommand);

			player.sendMessage(HeadEnchants.getConfigManager().getMessage("activation")
					.replaceAll("%upgrade%", "Key Hunter"));


		}
	}

}
