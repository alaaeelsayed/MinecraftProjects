package me.headshot.headtop.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    @EventHandler(priority= EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if (command.startsWith("/f top") || command.startsWith("/factions top")) {
            event.setMessage(event.getMessage().replaceAll("(/f top)|(/factions top)", "/ftop"));
        }
    }
}
