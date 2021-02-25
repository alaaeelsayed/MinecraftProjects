package me.headshot.headenchants.listeners;

import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.utils.type.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jul 30, 2015 6:43:34 PM
 */
public class ArmorListener implements Listener {

	public ArmorListener() {
	}

	@EventHandler
	public final void onInventoryClick(final InventoryClickEvent e) {
		boolean shift = false, numberkey = false;
		if (e.isCancelled())
			return;
		if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
			shift = true;
		}
		if (e.getClick().equals(ClickType.NUMBER_KEY)) {
			numberkey = true;
		}
		if ((e.getSlotType() != SlotType.ARMOR || e.getSlotType() != SlotType.QUICKBAR)
				&& !e.getInventory().getType().equals(InventoryType.CRAFTING))
			return;
		if (!(e.getWhoClicked() instanceof Player))
			return;
		if (e.getCurrentItem() == null)
			return;
		ItemType newArmorType = ItemType.fromItem(shift ? e.getCurrentItem() : e.getCursor());
		if (!shift && newArmorType != null && e.getRawSlot() != newArmorType.getDefaultSlot()) {
			// Used for drag and drop checking to make sure you aren't trying to
			// place a helmet in the boots place.
			return;
		}
		if (shift) {
			newArmorType = ItemType.fromItem(e.getCurrentItem());
			if (newArmorType != null) {
				boolean equipping = true;
				if (e.getRawSlot() == newArmorType.getDefaultSlot()) {
					equipping = false;
				}
				if (newArmorType.equals(ItemType.HELMET)
						&& (equipping == (e.getWhoClicked().getInventory().getHelmet() == null))
						|| newArmorType.equals(ItemType.CHESTPLATE)
						&& (equipping == (e.getWhoClicked().getInventory().getChestplate() == null))
						|| newArmorType.equals(ItemType.LEGGINGS)
						&& (equipping == (e.getWhoClicked().getInventory().getLeggings() == null))
						|| newArmorType.equals(ItemType.BOOTS)
						&& (equipping == (e.getWhoClicked().getInventory().getBoots() == null))) {
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(),
							ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(),
							equipping ? e.getCurrentItem() : null);
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if (armorEquipEvent.isCancelled()) {
						e.setCancelled(true);
					}
				}
			}
		} else {
			ItemStack newArmorPiece = e.getCursor();
			ItemStack oldArmorPiece = e.getCurrentItem();
			if (numberkey) {
				if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {// Prevents
					// shit
					// in
					// the
					// 2by2
					// crafting
					// e.getClickedInventory() == The players inventory
					// e.getHotBarButton() == key people are pressing to equip
					// or unequip the item to or from.
					// e.getRawSlot() == The slot the item is going to.
					// e.getSlot() == Armor slot, can't use e.getRawSlot() as
					// that gives a hotbar slot ;-;
					ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
					if (hotbarItem != null) {// Equipping
						newArmorType = ItemType.fromItem(hotbarItem);
						newArmorPiece = hotbarItem;
						oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
					} else {// Unequipping
						newArmorType = ItemType
								.fromItem(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR
										? e.getCurrentItem() : e.getCursor());
					}
				}
			} else {
				// e.getCurrentItem() == Unequip
				// e.getCursor() == Equip
				newArmorType = ItemType
						.fromItem(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR
								? e.getCurrentItem() : e.getCursor());
			}
			if (newArmorType != null && e.getRawSlot() == newArmorType.getDefaultSlot()) {
				ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.DRAG;
				if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey)
					method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), method, newArmorType,
						oldArmorPiece, newArmorPiece);
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
				if (armorEquipEvent.isCancelled()) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {

		if (e.getItem() == null) return;
		if (e.getAction() == Action.PHYSICAL)
			return;
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final Player player = e.getPlayer();
			if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {// Having
				// both
				// of
				// these
				// checks
				// is
				// useless,
				// might
				// as
				// well
				// do
				// it
				// though.
			}
			if (ItemType.fromItem(e.getItem()) != null) {
				ItemType newArmorType = ItemType.fromItem(e.getItem());
				if (newArmorType.equals(ItemType.HELMET) && e.getPlayer().getInventory().getHelmet() == null
						|| newArmorType.equals(ItemType.CHESTPLATE)
						&& e.getPlayer().getInventory().getChestplate() == null
						|| newArmorType.equals(ItemType.LEGGINGS) && e.getPlayer().getInventory().getLeggings() == null
						|| newArmorType.equals(ItemType.BOOTS) && e.getPlayer().getInventory().getBoots() == null) {
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR,
							ItemType.fromItem(e.getItem()), null, e.getItem());
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
					if (armorEquipEvent.isCancelled()) {
						e.setCancelled(true);
						player.updateInventory();
					}
				}
			}
		}
	}

	@EventHandler
	public void dispenserFireEvent(BlockDispenseEvent e) {
		ItemType type = ItemType.fromItem(e.getItem());
		if (ItemType.fromItem(e.getItem()) != null) {
			Location loc = e.getBlock().getLocation();
			for (Player p : loc.getWorld().getPlayers()) {
				if (loc.getBlockY() - p.getLocation().getBlockY() >= -1
						&& loc.getBlockY() - p.getLocation().getBlockY() <= 1) {
					if (p.getInventory().getHelmet() == null && type.equals(ItemType.HELMET)
							|| p.getInventory().getChestplate() == null && type.equals(ItemType.CHESTPLATE)
							|| p.getInventory().getLeggings() == null && type.equals(ItemType.LEGGINGS)
							|| p.getInventory().getBoots() == null && type.equals(ItemType.BOOTS)) {
						org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) e.getBlock().getState();
						org.bukkit.material.Dispenser dis = (org.bukkit.material.Dispenser) dispenser.getData();
						BlockFace directionFacing = dis.getFacing();
						// Someone told me not to do big if checks because it's
						// hard to read, look at me doing it -_-
						if (directionFacing == BlockFace.EAST && p.getLocation().getBlockX() != loc.getBlockX()
								&& p.getLocation().getX() <= loc.getX() + 2.3 && p.getLocation().getX() >= loc.getX()
								|| directionFacing == BlockFace.WEST && p.getLocation().getX() >= loc.getX() - 1.3
								&& p.getLocation().getX() <= loc.getX()
								|| directionFacing == BlockFace.SOUTH && p.getLocation().getBlockZ() != loc.getBlockZ()
								&& p.getLocation().getZ() <= loc.getZ() + 2.3
								&& p.getLocation().getZ() >= loc.getZ()
								|| directionFacing == BlockFace.NORTH && p.getLocation().getZ() >= loc.getZ() - 1.3
								&& p.getLocation().getZ() <= loc.getZ()) {
							ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DISPENSER,
									ItemType.fromItem(e.getItem()), null, e.getItem());
							Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
							if (armorEquipEvent.isCancelled()) {
								e.setCancelled(true);
							}
							return;
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void itemBreakEvent(PlayerItemBreakEvent e) {
		ItemType type = ItemType.fromItem(e.getBrokenItem());
		if (type != null) {
			Player p = e.getPlayer();
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
			if (armorEquipEvent.isCancelled()) {
				ItemStack i = e.getBrokenItem().clone();
				i.setAmount(1);
				i.setDurability((short) (i.getDurability() - 1));
				if (type.equals(ItemType.HELMET)) {
					p.getInventory().setHelmet(i);
				} else if (type.equals(ItemType.CHESTPLATE)) {
					p.getInventory().setChestplate(i);
				} else if (type.equals(ItemType.LEGGINGS)) {
					p.getInventory().setLeggings(i);
				} else if (type.equals(ItemType.BOOTS)) {
					p.getInventory().setBoots(i);
				}
			}
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		for (ItemStack i : p.getInventory().getArmorContents()) {
			if (i != null && !i.getType().equals(Material.AIR)) {
				Bukkit.getServer().getPluginManager()
						.callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ItemType.fromItem(i), i, null));
				// No way to cancel a death event.
			}
		}
	}
}
