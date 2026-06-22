package com.pet.animation;

import javax.swing.*;

import com.pet.behavior.MouseChasingBehavior;
import com.pet.core.PetContext;
import com.pet.core.constant.PetState;
import com.pet.resource.AnimationAssetsRegistry;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

/**
 * Manages animation playback for the pet.
 *
 * Responsibilities:
 * - Tracks the current animation state (idle, walking, sitting, playing, etc.)
 * - Advances animation frames over time
 * - Provides the current frame for rendering
 * - Notifies the renderer when a frame changes
 *
 * This class does NOT load animation assets, render graphics,
 * or control pet behaviors. It only manages animation state
 * and frame progression.
 *
 * Example:
 * IDLE    -> frame1 -> frame2 -> frame3 -> frame1
 * WALKING -> frame1 -> frame2 -> frame3 -> frame4
 * JUMPING -> frame1 -> frame2 -> frame3
 *
 * The renderer requests the current frame through
 * getCurrentFrame() and draws it on screen.
 */
public class AnimationManager {

    //FIXED :- now for state value petContext is used
    //private String currentState; // Current animation (idle, walk, sit)

    private int currentFrame; // Current frame index
    private Timer animationTimer; // Timer for frame updates
    private Runnable repaintCallback; // Panel to repaint

    // dependancy
    private final AnimationAssetsRegistry animationRegistry;
    private final PetContext petContext;


    public AnimationManager(
            PetContext petContext,
            Runnable repaintCallback,
            AnimationAssetsRegistry animationRegistry
    ) {
        this.petContext = petContext;
        this.repaintCallback = repaintCallback;


        this.animationRegistry = animationRegistry;

        this.currentFrame = 0;

        //register context
        petContext.addStateListener(this::onStateChanged);

        // Start animation timer
        startAnimationLoop();

    }

    private void onStateChanged(PetState state) {
        System.out.println(
                "ANIMATION_MANAGER :- State changed -> " + state
        );

        currentFrame = 0;

    }

    private void startAnimationLoop() {
        animationTimer = new Timer();
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Update to next frame
                currentFrame = (currentFrame + 1) % getCurrentAnimation().length;
                // Repaint the display
                // System.out.println("START_ANIMATION :- Repainting panel... with frame " + currentFrame);
                repaintCallback.run();
            }
        }, 0, 200); // Update every 200ms (5 FPS)
    }


    // get the current frame image
    public Image getCurrentFrame() {
        Image[] frames = getCurrentAnimation();
        if (frames != null && frames.length > 0) {
            return frames[currentFrame];
        }
        return null;
    }

    // get the current animation frames
    private Image[] getCurrentAnimation() {

        return animationRegistry.getFrames(
                petContext.getState()
        );
    }

    public String getCurrentState() {
        return petContext.getState().toString();
    }

    public void dispose() {
        if (animationTimer != null) {
            animationTimer.cancel();
        }

    }

}
