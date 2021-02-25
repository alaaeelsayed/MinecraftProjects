package me.headshot.headcrates;

import me.headshot.headcrates.crate.Crate;
import me.headshot.headcrates.crate.CrateItem;
import me.headshot.headcrates.render.AnimationRenderer;
import me.headshot.headcrates.util.CrateUtil;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class CrateRoller {
    private static Map<String, Crate> crates = new HashMap<>();

    public static void roll(MapView mapView, CrateWinRunnable done, Crate crate) {
        for (MapRenderer renderer : mapView.getRenderers())
            mapView.removeRenderer(renderer);
        new Thread(() -> {
            Map<List<CrateItem>, CrateItem> chosen = CrateUtil.getRandomItem(crate);
            mapView.addRenderer(new AnimationRenderer(done, CrateUtil.crateToImages(chosen), chosen.entrySet().iterator().next().getValue()));
        }).start();
    }

    public static Map<String, Crate> getCrates() {
        return crates;
    }

    public static void addCrate(String name, Crate crate) {
        crates.put(name, crate);
    }

    public static Crate getCrate(String name) {
        return crates.get(name.toLowerCase());
    }

    public static void removeCrate(String name) {
        crates.remove(name);
    }
}
