package me.headshot.headcrates.render;

import me.headshot.headcrates.CrateWinRunnable;
import me.headshot.headcrates.HeadCrates;
import me.headshot.headcrates.crate.CrateItem;
import me.headshot.headcrates.util.CrateUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.*;
import org.bukkit.scheduler.BukkitTask;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class AnimationRenderer extends MapRenderer {

    public CrateWinRunnable runnable;
    private byte[][] bufferedImages;
    public CrateItem wonItem;
    private int index = 0;
    public int taskID;
    public BukkitTask laterTask = null;
    private boolean animationRendered = false;
    public MapView latestView = null;

    public AnimationRenderer(CrateWinRunnable runnable, byte[][] images, CrateItem wonItem) {
        this.runnable = runnable;
        this.bufferedImages = images;
        this.wonItem = wonItem;
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        canvas.setCursors(new MapCursorCollection());
        if (animationRendered) {
            return;
        }
        latestView = view;
        animationRendered = true;
        CrateUtil.addUsing(player, this);
        taskID = Bukkit.getScheduler().runTaskTimer(HeadCrates.getInstance(), () -> {
            if (index >= bufferedImages.length) {
                laterTask = Bukkit.getScheduler().runTaskLaterAsynchronously(HeadCrates.getInstance(), () -> {
                    for (ItemStack item : player.getInventory().getContents())
                        if (item != null && item.getType() == Material.MAP && item.getDurability() == view.getId())
                            player.getInventory().remove(item);
                    Bukkit.getScheduler().runTask(HeadCrates.getInstance(), () -> {
                        runnable.run(wonItem);
                        CrateUtil.removeUsing(player);
                    });
                }, 40);
                Bukkit.getScheduler().cancelTask(taskID);
                return;
            }
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3, 3);
            byte[] bytes = bufferedImages[index];
            for (int x2 = 0; x2 < 128; ++x2) {
                for (int y2 = 0; y2 < 128; ++y2) {
                    canvas.setPixel(x2, y2, bytes[y2 * 128 + x2]);
                }
            }
            index++;
        }, 0, 5).getTaskId();
    }
}