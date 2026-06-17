package com.pet.resource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public final class AssetManager {

    /**
     * Load image from resources.
     *
     * Returns fallback image if resource is missing.
     */
    public Image loadImage(String path) {

        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println(
                    "LOAD_IMAGE :- Resource not found: " + path
            );

            return createFallbackImage();
        }

        return new ImageIcon(resource).getImage();
    }

    /**
     * Load icon from resources.
     *
     * Returns fallback icon if resource is missing.
     */
    public ImageIcon loadIcon(String path) {

        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println(
                    "LOAD_ICON :- Resource not found: " + path
            );

            return createFallbackIcon();
        }

        return new ImageIcon(resource);
    }

    /**
     * Generates a simple fallback image
     * Used when image asset cannot be loaded.
     */
    private Image createFallbackImage() {

        BufferedImage img =
                new BufferedImage(
                        128,
                        128,
                        BufferedImage.TYPE_INT_ARGB
                );

        Graphics2D g = img.createGraphics();

        g.setColor(Color.RED);
        g.fillRect(0, 0, 128, 128);

        g.setColor(Color.WHITE);
        g.drawString("Missing", 35, 64);

        g.dispose();

        return img;
    }

    /**
     * Generates a simple fallback application icon.
     *
     * Used when icon asset cannot be loaded.
     */
    private ImageIcon createFallbackIcon() {

        BufferedImage img =
                new BufferedImage(
                        64,
                        64,
                        BufferedImage.TYPE_INT_ARGB
                );

        Graphics2D g = img.createGraphics();

        g.setColor(new Color(139, 69, 19));
        g.fillOval(10, 20, 44, 30);

        g.fillOval(40, 5, 20, 20);

        g.setColor(Color.BLACK);

        g.fillOval(45, 10, 5, 5);
        g.fillOval(55, 10, 5, 5);

        g.dispose();

        return new ImageIcon(img);
    }
}