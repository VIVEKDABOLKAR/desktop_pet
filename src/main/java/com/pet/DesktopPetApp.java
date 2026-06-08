package com.pet;

// FOR LETTER TASK SERCH TASK :==

import com.pet.animation.AnimationManager;
import com.pet.animation.physics.DesktopPhysics;
import com.pet.animation.physics.PetBody;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.InputStream;

public class DesktopPetApp extends JFrame {
    private Image dogImage;
    private Point dragOffset;

    private AnimationManager animationManager;
    private DesktopPhysics physics;
    private JPanel panel;

    private Timer updateTimer;
    private Timer topMostTimer;

    // flag
    private boolean isPhysicsEnabled = true; // to enable/disable physics
    private boolean isAnimationEnabled = true; // to enable/disable animation

    public DesktopPetApp() {
        // Basic window setup
        setSize(128, 128);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0));
        setLocation(100, 100);
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/dog_icon.png"));
        setIconImage(icon.getImage());
        

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

        // Load dog image from resources
        try {
            // This loads from src/main/resources/assets/
            InputStream is = getClass().getResourceAsStream("/assets/dog_idle_2.png");
            if (is != null) {
                dogImage = ImageIO.read(is);
            } else {
                System.err.println("MAIN :- Could not find dog_idle.png in resources");
                dogImage = createFallbackDog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            dogImage = createFallbackDog();
        }

        // Create transparent panel
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) { // THIS FUNCTION CALL EVERY TIME WE CALL REPAINT
                setOpaque(false);
                Image currentFrame = animationManager.getCurrentFrame();
                if (currentFrame != null) {
                    // System.out.println("MAIN-JPANEL :- Drawing current animation frame...");
                    g.drawImage(currentFrame, 0, 0, getWidth(), getHeight(), null);
                } else if (dogImage != null) {
                    System.out.println("MAIN-JPANEL :-Drawing dog image...");
                    g.drawImage(dogImage, 0, 0, getWidth(), getHeight(), null);
                } else {
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
        physics = new DesktopPhysics();
        animationManager = new AnimationManager(panel, physics);

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
    }


    private void createFallbackIcon() {
        // Create a simple colored icon as fallback
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        // Draw a simple dog icon
        g.setColor(new Color(139, 69, 19)); // Brown
        g.fillOval(10, 20, 44, 30); // Body
        g.fillOval(40, 5, 20, 20);  // Head
        g.setColor(Color.BLACK);
        g.fillOval(45, 10, 5, 5);   // Eye
        g.fillOval(55, 10, 5, 5);   // Eye
        
        g.dispose();
        setIconImage(img);
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
        String[] states = {
                AnimationManager.IDLE,
                AnimationManager.WALKING,
                AnimationManager.SITTING,
                AnimationManager.PLAYING
        };

        String currentState = animationManager.getCurrentState();
        System.out.println("woof... woof..");
        for (int i = 0; i < states.length; i++) {
            if (states[i].equals(currentState)) {
                String nextState = states[(i + 1) % states.length];
                animationManager.setAnimationState(nextState);
                break;
            }
        }
        System.out.println("Current State: " + animationManager.getCurrentState());
    }

    private Image createFallbackDog() {
        // Simple black dog as fallback
        BufferedImage img = new BufferedImage(128, 128,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        g.fillOval(30, 50, 68, 48);
        g.fillOval(80, 30, 40, 40);
        g.dispose();
        return img;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DesktopPetApp();
        });
    }

    @Override
    public void dispose() {
        if (updateTimer != null)
            updateTimer.stop();

        animationManager.dispose();

        if (topMostTimer != null)
            topMostTimer.stop();

        if (dogImage != null)
            dogImage.flush();

        super.dispose();
        System.exit(0);
    }
}