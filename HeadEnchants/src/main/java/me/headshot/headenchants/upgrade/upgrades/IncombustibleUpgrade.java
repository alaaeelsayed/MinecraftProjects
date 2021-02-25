package me.headshot.headenchants.upgrade.upgrades;

import me.headshot.headenchants.HeadEnchants;
import me.headshot.headenchants.events.ArmorEquipEvent;
import me.headshot.headenchants.upgrade.Upgrade;
import me.headshot.headenchants.utils.type.GearType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IncombustibleUpgrade extends Upgrade {

    public IncombustibleUpgrade(int level) {
        super("incombustible", level, 1, HeadEnchants.getConfigManager().getLore("incombustible"), GearType.ARMOR.getItemTypes());

        if (lore == null) lore = HeadEnchants.getConfigManager().getLore(this);
    }

    @Override
    public String getDefaultLore() {
        return "Incombustible %level%";
    }

    @Override
    public void onWear(ArmorEquipEvent event, Player player, boolean removed) {
        if (!removed) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, getLevel() - 1));
        } else {
            event.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
    }

    @Override
    public void onSwitch(PlayerItemHeldEvent event, boolean off) {

    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event, Player player) {

    }

    @Override
    public void onEvent(Event event, Player player) {
    }
}
