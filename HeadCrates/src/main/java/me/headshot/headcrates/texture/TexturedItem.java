package me.headshot.headcrates.texture;

import me.headshot.headcrates.crate.CrateItem;
import me.headshot.headcrates.crate.CrateTier;

import java.awt.image.BufferedImage;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class TexturedItem {
    private final CrateItem crateItem;
    private final BufferedImage texture;
    private final CrateTier tier;

    public TexturedItem(CrateItem item, BufferedImage texture, CrateTier tier){
        this.crateItem = item;
        this.texture = texture;
        this.tier = tier;
    }

    public CrateItem getCrateItem() {
        return crateItem;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public CrateTier getTier() {
        return tier;
    }
}
