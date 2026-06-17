package com.pet.windows;

import javax.swing.JFrame;
import java.awt.*;

/**
 * Details of pet screen and some default config for pet windows
 * now this is over main windws 
 */
public class PetWindow extends JFrame {

    public PetWindow() {
        configureWindow();
    }

    private void configureWindow() {
        setSize(128,128);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0,0,0,0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}