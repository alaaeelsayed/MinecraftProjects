package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class ObsidianDestroyerUpgrade extends Upgrade {

	public ObsidianDestroyerUpgrade(int level) {
		super("obsidian-destroyer", level, 1, HeadEnchants.getConfigManager().getLore("obsidian-destroyer"), ItemType.PICKAXE);

		if (lore == null) {
			lore = HeadEnchants.getConfigManager().getLore(this);
		}
	}

	@Override
	public String getDefaultLore() {
		return "Obsidian Destroyer %level%";
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
		if(event instanceof PlayerInteractEvent){
			PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
			if(playerInteractEvent.getAction() != Action.LEFT_CLICK_BLOCK) return;
			Block breakingBlock = playerInteractEvent.getClickedBlock();
			if(breakingBlock.getType() != Material.OBSIDIAN) return;
			// Adding mcMMO is too much work

//			FakeBlockBreakEvent blockBreakEvent = new FakeBlockBreakEvent(breakingBlock, player);
//			Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
//			if(!blockBreakEvent.isCancelled())
//				breakingBlock.setType(Material.AIR);

		}
	}

}
