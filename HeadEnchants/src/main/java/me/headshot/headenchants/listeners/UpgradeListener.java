package me.headshot.headenchants.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.upgrade.UpgradeManager;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.headshot.headenchants.events.ArmorEquipEvent;


public class UpgradeListener implements Listener {
	private UpgradeManager manager;

	public UpgradeListener() {
		manager = HeadEnchants.getUpgradeManager();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		manager.performEvent(event, event.getPlayer(), event.getPlayer().getItemInHand());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent event) {
		manager.performEvent(event, event.getPlayer(), event.getItemDrop().getItemStack());
	}


	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		if(event.getEntity().getKiller() == null) return;
		manager.performEvent(event, event.getEntity().getKiller(), event.getEntity().getKiller().getItemInHand());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onItemHeld(PlayerItemHeldEvent event) {
		ItemStack oldItem = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
		ItemStack newItem = event.getPlayer().getInventory().getItem(event.getNewSlot());
		manager.performSwitch(event, oldItem, newItem);
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		manager.performEvent(event, event.getPlayer(), event.getPlayer().getItemInHand());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onClick(InventoryClickEvent event){
		manager.performEvent(event, (Player)event.getWhoClicked(), event.getWhoClicked().getItemOnCursor());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
			Player p = (Player) event.getDamager();
			manager.performDamage(event, p, p.getItemInHand());
			manager.performDamage(event, p);
		}
	}

	public static HashMap<UUID, ItemStack> lastBow = new HashMap<UUID, ItemStack>();
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamagePlayer(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof LivingEntity && event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			manager.performDamage(event, p);
		}else if(event.getDamager() instanceof Arrow){
						
			Arrow a = (Arrow) event.getDamager();
			
			if(!(a.getShooter() instanceof Player)) return; //Not a player
			
			Player shooter = (Player) a.getShooter();
			for(Upgrade upgrade : HeadEnchants.getUpgradeManager().getUpgrades(shooter.getItemInHand()))
				for (ItemType itemType : upgrade.getTypes())
					if(itemType == ItemType.BOW){
						manager.performDamage(event, shooter, shooter.getItemInHand());
						return;
					}
			if(lastBow.containsKey(shooter.getUniqueId()))
				manager.performDamage(event, shooter, lastBow.get(shooter.getUniqueId()));
			
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemInHandSwitchFromBow(PlayerItemHeldEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (item == null) return;

		if(item.getType() == Material.BOW) lastBow.put(event.getPlayer().getUniqueId(), item);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onArmorWorn(ArmorEquipEvent event) {
		Player player = event.getPlayer();
		ItemStack item = null;
		if(event.getOldArmorPiece() != null && event.getOldArmorPiece().getType() != Material.AIR){
			item = event.getOldArmorPiece();
			manager.performArmor(event, player, true, item);
		}
		if(event.getNewArmorPiece() != null && event.getNewArmorPiece().getType() != Material.AIR){
			item = event.getNewArmorPiece();
			manager.performArmor(event, player, false, item);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPotion(PotionSplashEvent event) {
		for (LivingEntity entity : event.getAffectedEntities()) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				manager.performEvent(event, player, player.getInventory().getArmorContents());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onConsume(PlayerItemConsumeEvent event) {
		manager.performEvent(event, event.getPlayer(), event.getPlayer().getInventory().getArmorContents());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent event) {
		manager.performEvent(event, event.getEntity(), event.getEntity().getInventory().getArmorContents());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		manager.performEvent(event, ((Player) event.getEntity()),
				((Player) event.getEntity()).getInventory().getArmorContents());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		manager.performEvent(event, event.getPlayer(), event.getItemInHand());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location to = event.getTo();
		if (to.getBlock().getType() == Material.STONE_PLATE
				|| to.getBlock().getType() == Material.GOLD_PLATE) {
			if (!to.getBlock().hasMetadata("isLandmine"))
				return;
			if (to.getBlock().getMetadata("isLandmine").get(0).asBoolean() == (true)) {
				if (player.getUniqueId().toString()
						.equals(to.getBlock().getMetadata("placer").get(0).asString()))
					return;
				int landmineLevel = to.getBlock().getMetadata("landMineLevel").get(0)
						.asInt();
				if (landmineLevel == 1) {
					player.damage(6);
					player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 5);
				}
				if (landmineLevel == 2) {
					player.damage(10);
					player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 5);
				}
				if (landmineLevel == 3) {
					player.damage(18);
					player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 5);
				}
				to.getBlock().setType(Material.AIR);
				to.getBlock().setMetadata("isLandmine", new FixedMetadataValue(HeadEnchants.get(), false));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!HeadEnchants.getUpgradeManager().canPlayerMove(event.getPlayer().getUniqueId()))
			event.setCancelled(true);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemInHandSwitch(PlayerItemHeldEvent event) {
		ItemStack lastStack = HeadEnchants.getUpgradeManager().getLastStack(event.getPlayer().getUniqueId());
		if (lastStack == null)
			return;
		// Remove haste effect
		if (!lastStack.isSimilar(event.getPlayer().getItemInHand())) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 0, 1, true));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 0, 2, true));
		}
		HeadEnchants.getUpgradeManager().removeLastStack(event.getPlayer().getUniqueId());
	}
	
	public static Set<Player> hiddenPlayers = new HashSet<Player>();
		
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		for(Player p : hiddenPlayers){
			
			event.getPlayer().hidePlayer(p);
			
		}
	}
	
	Set<TNTPrimed> noTNTDamage = new HashSet<TNTPrimed>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onTNTDamage(EntityDamageByEntityEvent event){
		if(!(event.getDamager() instanceof TNTPrimed)) return;
		if(!(event.getEntity() instanceof Player)) return;
		
		TNTPrimed tnt = (TNTPrimed) event.getDamager();
		if(noTNTDamage.contains(tnt)) event.setCancelled(true);
	}
	
	Set<UUID> noEnderPearlTP = new HashSet<UUID>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEnderpearlTP(PlayerTeleportEvent event){
		if(event.getCause() == TeleportCause.ENDER_PEARL && noEnderPearlTP.contains(event.getPlayer().getUniqueId())) event.setCancelled(true);
	}
			
}
