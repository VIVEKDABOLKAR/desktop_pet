package com.pet;

// FOR LETTER TASK SERCH TASK :==

import com.pet.animation.AnimationManager;
import com.pet.animation.enviroment.EnvironmentManager;
import com.pet.animation.physics.PetBody;
import com.pet.animation.physics.PhysicsEngine;
import com.pet.behavior.BehaviorManager;
import com.pet.core.PetContext;
import com.pet.core.constant.PetState;
import com.pet.resource.AnimationAssetsRegistry;
import com.pet.resource.AssetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class DesktopPetAppV2 extends JFrame {
    private Image dogImage;
    private Point dragOffset;

    private AnimationManager animationManager;
    private PhysicsEngine physics;
    private JPanel panel;

    private Timer updateTimer;
    private Timer topMostTimer;

    // flag
    private boolean isPhysicsEnabled = true; // to enable/disable physics
    private boolean isAnimationEnabled = true; // to enable/disable animation

    //di
    private AssetManager assetManager = new AssetManager();
    private PetContext petContext;
    private BehaviorManager behaviorManager;
    private EnvironmentManager environmentManager;

    public DesktopPetAppV2() {
        // Basic window setup
        setSize(128, 128);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0));
        setLocation(100, 100);

        //petcontext
        petContext = new PetContext();

        /*old icon load
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/dog_icon.png"));
        setIconImage(icon.getImage());
        */

        //new icon load
        ImageIcon icon1 = assetManager.loadIcon("/assets/dog_icon.png");
        setIconImage(icon1.getImage());

        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // No action needed
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // Re-assert "Top" priority when focus is lost to another app
                if (e.getNewState() != WindowEvent.WINDOW_CLOSED) {
                    setAlwaysOnTop(false);
                    setAlwaysOnTop(true);
                }
            }
        });


        // Create transparent panel
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) { // THIS FUNCTION CALL EVERY TIME WE CALL REPAINT
                setOpaque(false);
                Image currentFrame = animationManager.getCurrentFrame();
                if (currentFrame != null) {
                    // System.out.println("MAIN-JPANEL :- Drawing current animation frame...");
                    g.drawImage(currentFrame, 0, 0, getWidth(), getHeight(), null);
                }  else {
                    System.err.println("MAIN-JPANEL :- No frame to draw!");
                    g.setColor(Color.RED);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                //temp - rempve after plllll
                g.setColor(Color.GREEN);
                g.drawRect(
                    0,
                    0,
                    PetBody.WIDTH,
                    PetBody.HEIGHT
                );

                setOpaque(false);
            }
        };

        add(panel);

        // Initialize Physics, Animation Manager
        physics = new PhysicsEngine();
        animationManager = new AnimationManager(
                petContext,
                panel::repaint,
                new AnimationAssetsRegistry(assetManager)
        );
        behaviorManager = new BehaviorManager(
                petContext,
                physics
        );

        // set init positon
        physics.setPosition(
                (float) (Math.random() * (physics.getScreenBound().width - 100)),
                (float) (Math.random() * (physics.getScreenBound().height - 100)));

        // setup interaction
        setupMouseListener();

        // start update loop for physics and repaint
        startUpdateLoop();

        // TASK :== imporve this because it call use so much cpu, tip:-1 use adpative
        // timer , tip 2:- use native code to set always on top
        topMostTimer = new Timer(1000, e -> {
            if (!isAlwaysOnTop()) {
                setAlwaysOnTop(true);
            }
            // toFront(); use it when it fails to bring to front
        });
        topMostTimer.start();

        setVisible(true);

        this.environmentManager = new EnvironmentManager(
            physics
        );
    }


    // update loop for physics and repaint - updateTimer
    private void startUpdateLoop() {
        updateTimer = new Timer(16, e -> {
            if (isPhysicsEnabled) {

                // get current position
                Point oldPos = getLocation();

                // update physics
                physics.update();

                // update window position
                Point newPos = physics.getPosition();

                // sel location is required
                if (oldPos.x != newPos.x || oldPos.y != newPos.y)
                    setLocation(newPos.x, newPos.y);

            }
        });
        updateTimer.start();
    }

    private void setupMouseListener() {

        // Make window draggable
        addMouseListener(new MouseAdapter() {
            // to prevent physics update during interaction

            @Override
            public void mousePressed(MouseEvent e) {
                isPhysicsEnabled = false;
                dragOffset = e.getPoint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TASK :== add timmer to enable physics after some time
                isPhysicsEnabled = true;
            }

            // handle mouse click
            @Override
            public void mouseClicked(MouseEvent e) {
                // if left button clicked extit application
                if (e.getButton() == MouseEvent.BUTTON3) {
                    System.exit(0);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    handleClickInteraction(animationManager);
                    isPhysicsEnabled = true;
                }
            }
        });

        // mouse motion listener for dragging
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                isPhysicsEnabled = false;
                Point current = getLocation();
                float newX = current.x + e.getX() - dragOffset.x;
                float newY = current.y + e.getY() - dragOffset.y;
                physics.setPosition(newX, newY);
                setLocation((int) newX, (int) newY);
            }
        });
    }

    // handle click interaction <- mouesClicked calls this
    private void handleClickInteraction(AnimationManager animationManager) {
        java.util.List<PetState> states = java.util.List.of(
                PetState.IDLE,
                PetState.WALKING,
                PetState.SITTING,
                PetState.PLAYING
        );

        //get current state
        //find index of that state
        //increase state by one (handle overflow)
        int nextIdx = (states.indexOf(petContext.getState()) + 1) % states.size();

        //set new state
        petContext.setState(states.get(nextIdx));

        System.out.println("woof... woof..");

        System.out.println("Current State: " + animationManager.getCurrentState());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DesktopPetAppV2();
        });
    }

    @Override
    public void dispose() {
        if (updateTimer != null)
            updateTimer.stop();

        animationManager.dispose();

        behaviorManager.dispose();

        if (topMostTimer != null)
            topMostTimer.stop();

        if (dogImage != null)
            dogImage.flush();

        super.dispose();
        System.exit(0);
    }
}