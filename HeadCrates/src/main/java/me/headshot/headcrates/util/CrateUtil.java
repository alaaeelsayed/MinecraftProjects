package me.headshot.headcrates.util;

import me.headshot.headcrates.CrateRoller;
import me.headshot.headcrates.config.HeadCratesConfig;
import me.headshot.headcrates.crate.Crate;
import me.headshot.headcrates.crate.CrateItem;
import me.headshot.headcrates.render.AnimationRenderer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class CrateUtil {

    private static Map<UUID, AnimationRenderer> using = new HashMap<>();

    public static boolean isUsing(OfflinePlayer player) {
        return using.containsKey(player.getUniqueId());
    }

    public static void addUsing(OfflinePlayer player, AnimationRenderer renderer) {
        using.put(player.getUniqueId(), renderer);
    }

    public static AnimationRenderer getRenderer(OfflinePlayer player) {
        return using.get(player.getUniqueId());
    }

    public static void removeUsing(OfflinePlayer player) {
        using.remove(player.getUniqueId());
    }

    public static void initCrates(HeadCratesConfig config) {
        for (String crateName : config.getConfig().getConfigurationSection("crates").getKeys(false)) {
            CrateRoller.addCrate(crateName.toLowerCase(), Crate.fromName(crateName));
        }
    }

    public static boolean isCrate(ItemStack item) {
        if (!item.hasItemMeta() || item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasLore() && meta.getLore().equals(Collections.singletonList(ChatColor.GRAY + "An incredible crate forged with godly power!"));
    }

    public static boolean isRollingCrate(ItemStack item) {
        if (!item.hasItemMeta() || item.getItemMeta() == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() && meta.getDisplayName().startsWith(ChatColor.RED + "Rolling");
    }

    public static Map<List<CrateItem>, CrateItem> getRandomItem(Crate crate) {
        List<CrateItem> crateItems = crate.getCrateItems();
        Collections.shuffle(crateItems, new Random());
        Map<CrateItem, Double> crateWeights = crateItems.stream().collect(Collectors.toMap(crateItem -> crateItem, crateItem -> (double) crateItem.getPercentage()));
        CrateItem chosenItem = getWeightedRandom(crateWeights.entrySet().stream(), new Random());
        Map<List<CrateItem>, CrateItem> wonItem = new HashMap<>();
        wonItem.put(crateItems, chosenItem);
        return wonItem;
    }

    // Can be improved with an image cache

    public static byte[][] crateToImages(Map<List<CrateItem>, CrateItem> wonItem) {
        Map.Entry<List<CrateItem>, CrateItem> entry = wonItem.entrySet().iterator().next();
        List<CrateItem> crateItems = entry.getKey();
        CrateItem chosenItem = entry.getValue();

        int imageSize = 16384;

        byte[] fullImageSequence = new byte[crateItems.size() * imageSize];
        int writePosition = 0;
        for (CrateItem item : crateItems) {
            byte[] img = item.getCrateItemImg();
            System.arraycopy(img, 0, fullImageSequence, writePosition, imageSize);
            writePosition += imageSize;
        }

        int lastImageIndex = crateItems.indexOf(chosenItem);
        int totalOutputImages = (lastImageIndex * 4) + 1;
        byte[][] finalImages = new byte[totalOutputImages][imageSize];
        int stride = imageSize / 4;

        for (int imageSequence = 0; imageSequence < totalOutputImages; ++imageSequence) {
            System.arraycopy(fullImageSequence, imageSequence * stride, finalImages[imageSequence], 0, imageSize);
        }

        return finalImages;
    }

    private static <E> E getWeightedRandom(Stream<Map.Entry<E, Double>> weights, Random random) {
        return weights
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), -Math.log(random.nextDouble()) / e.getValue()))
                .min(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
                .orElseThrow(IllegalArgumentException::new).getKey();
    }
}