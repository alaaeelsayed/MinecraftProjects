package me.headshot.headcrates.util;


import java.awt.*;
import java.awt.image.BufferedImage;
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
public class ImageUtil {

    public static BufferedImage joinBufferedImages(List<BufferedImage> images) {
        BufferedImage result = ImageUtil.toCompatibleImage(new BufferedImage(
                images.size() * 128, 128,
                BufferedImage.TYPE_INT_RGB));
        Graphics2D graphics = result.createGraphics();
        int x = 0;
        for (BufferedImage image : images) {
            graphics.drawImage(image, x, 0, null);
            x += 128;
        }
        graphics.dispose();
        return result;
    }

    public static BufferedImage toCompatibleImage(BufferedImage image) {
        // obtain the current system graphical settings
        GraphicsConfiguration gfx_config = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();

    /*
     * if image is already compatible and optimized for current system
     * settings, simply return it
     */
        if (image.getColorModel().equals(gfx_config.getColorModel()))
            return image;

        // image is not optimized, so create a new image that is
        BufferedImage new_image = gfx_config.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return new_image;
    }

}
