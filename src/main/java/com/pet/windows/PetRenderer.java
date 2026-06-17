package com.pet.windows;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.pet.animation.AnimationManager;

public class PetRenderer extends JPanel {
    
    private final AnimationManager animationManager;
    private final Image fallbackImage;

    public PetRenderer(
        AnimationManager animationManager,
        Image fallbackImage
    ) {
        this.animationManager = animationManager;
        this.fallbackImage = fallbackImage;
        //make panel transparent
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image frame = animationManager.getCurrentFrame();

        if(frame != null){
            g.drawImage(frame,0,0,getWidth(),getHeight(),null);
        }
    }
}