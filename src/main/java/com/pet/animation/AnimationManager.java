package com.pet.animation;

import javax.swing.*;

import com.pet.animation.physics.DesktopPhysics;
import com.pet.behavior.MouseChasingBehavior;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class AnimationManager {
    private Map<String, Image[]> animationStates; // Store animation frames
    private String currentState; // Current animation (idle, walk, sit)
    private int currentFrame; // Current frame index
    private Timer animationTimer; // Timer for frame updates
    private JPanel displayPanel; // Panel to repaint

    // dependancy
    private DesktopPhysics physics;
    private MouseChasingBehavior mouseChasingBehavior;

    // behavior flags
    private boolean isChasingMouse = false;

    // Animation states
    public static final String IDLE = "idle";
    public static final String WALKING = "walking";
    public static final String SITTING = "sitting";
    public static final String PLAYING = "playing";

    public AnimationManager(JPanel panel, DesktopPhysics physics) {
        this.physics = physics;
        this.animationStates = new HashMap<>();
        this.currentState = IDLE;
        this.currentFrame = 0;
        this.displayPanel = panel;


        // Initialize all animation states
        initializeAnimations();

        // Start animation timer
        startAnimationLoop();

    }

    private void startBehavoir() {
        behaviorFluse();
        switch (currentState) {
            // for now for ideal, wlaking, sitting behaviour is handle by default animation loop
            case IDLE:
                break;
            case WALKING:
                break;
            case SITTING:
                break;
            case PLAYING:
                isChasingMouse = true;
                mouseChasingBehavior();
                break;
            default:
                break;
        }
    }

    private void mouseChasingBehavior() {
        System.out.println("ANIMATION_MANAGER :- Starting Mouse Chasing Behavior");
        MouseChasingBehavior mouseChasingBehavior = new MouseChasingBehavior(physics, this);
        this.mouseChasingBehavior = mouseChasingBehavior;
    }

    // add image to the animationStates map
    private void initializeAnimations() {
        // Idle animation (2 frames for subtle movement)
        animationStates.put(IDLE, new Image[] {
                loadImage("/assets/idle/dog_idle_01.png"),
                loadImage("/assets/idle/dog_idle_02.png"),
                loadImage("/assets/idle/dog_idle_03.png"),
                loadImage("/assets/idle/dog_idle_04.png")
        });

        List<Image> walkingFrames = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 3 && j == 1 || i == 3 && j == 2 || i == 4 && j == 3 || i == 4 && j == 2) {
                    // Skip frame unnamed_4_3 as it does not exist
                    continue;
                }
                try {
                    walkingFrames.add(
                            loadImage("/assets/walking/walk_temp/unnamed_" + i + "_" + j + "-Photoroom.png"));
                } catch (Exception e) {
                    System.err.println("INIT_ANIMATION :- Failed to load walking frame " + i + "_" + j);
                }
            }
        }

        animationStates.put(WALKING, walkingFrames.toArray(new Image[0]));

        // Sitting animation
        animationStates.put(SITTING, new Image[] {
                loadImage("/assets/dog_idle_1.png"),
                // loadImage("/assets/dog_sit_2.png") // Optional: slight breathing motion
        });

        // Playing animation
        animationStates.put(PLAYING,
            // TASK :== INSTED OF WALKING FRAME CHANGE IT WITH PLAYING FRAM ,  FOR NOW USE REDUENTED WALKING ANIMATION
            walkingFrames.toArray(new Image[0])
        );
    }

    // load image from resources
    private Image loadImage(String path) {
        try {
            return new ImageIcon(getClass().getResource(path)).getImage();
        } catch (Exception e) {
            System.err.println("LOAD_IMAGE :- Failed to load image: " + path);
            return createFallbackImage();
        }
    }

    // create a simple fallback image
    private Image createFallbackImage() {
        // Simple colored square as fallback
        BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 128, 128);
        g.setColor(Color.WHITE);
        g.drawString("Missing", 40, 60);
        g.dispose();
        return img;
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
                displayPanel.repaint();
            }
        }, 0, 200); // Update every 200ms (5 FPS)
    }

    // set the current animation state
    public void setAnimationState(String state) {
        if (animationStates.containsKey(state) && !currentState.equals(state)) {
            currentState = state;
            currentFrame = 0; // Reset to first frame
            startBehavoir();
        }
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
        return animationStates.get(currentState);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void dispose() {
        if (animationTimer != null) {
            animationTimer.cancel();
        }

        if (mouseChasingBehavior != null) {
            mouseChasingBehavior.dispose();
        }
    }

    private void behaviorFluse() {
        if (isChasingMouse) {
            isChasingMouse = false;
            if (mouseChasingBehavior != null) {
                mouseChasingBehavior.dispose();
                mouseChasingBehavior = null;
            }
        }
    }
}
