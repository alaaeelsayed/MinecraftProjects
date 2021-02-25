package me.headshot.headcrates.crate;

import me.headshot.headcrates.HeadCrates;
import me.headshot.headcrates.text.TextAlignment;
import me.headshot.headcrates.text.TextFormat;
import me.headshot.headcrates.text.TextRenderer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapPalette;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * ********************************
 * Created by Headshot on 12/2/2016.
 * <p>
 * <p>
 * ********************************
 * DO NOT USE WITHOUT PERMISSION.
 * ********************************
 */
public class CrateItem {
    private final String id;
    private final ItemStack item;
    private final int meta;
    private final String name;
    private final List<String> commands;
    private final int percentage;
    private final CrateTier tier;
    private byte[] image;

    CrateItem(String id, ItemStack item, int meta, String name, List<String> commands, int percentage, CrateTier tier) {
        this.id = id;
        this.item = item;
        this.meta = meta;
        this.name = name;
        this.commands = commands;
        this.percentage = percentage;
        this.tier = tier;
        initImg();
    }

    public String getID() {
        return id;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getPercentage() {
        return percentage;
    }

    private void initImg() {
        try {
            BufferedImage itemImage = ImageIO.read(HeadCrates.getInstance().getResource("items/" + item.getType().getId() + "-" + meta + ".png"));
            BufferedImage tierImage = ImageIO.read(HeadCrates.getInstance().getResource("tiers/" + tier.toString().toLowerCase() + ".png"));
            tierImage.getGraphics().drawImage(itemImage, 40, 25, 50, 50, null);
            Rectangle bounds = new Rectangle(10, 57, tierImage.getWidth() - 20, tierImage.getHeight() - 20);
            TextRenderer.drawString(tierImage.getGraphics(), name, HeadCrates.getInstance().mcFont.deriveFont(Font.BOLD).deriveFont(12f), Color.WHITE,
                    bounds, TextAlignment.MIDDLE, TextFormat.FIRST_LINE_VISIBLE);
            tierImage.getGraphics().dispose();
            image = MapPalette.imageToBytes(tierImage);
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
    }


    public byte[] getCrateItemImg() {
        return image;
    }
}
