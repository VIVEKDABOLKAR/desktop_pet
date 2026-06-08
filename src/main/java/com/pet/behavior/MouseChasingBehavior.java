package com.pet.behavior;

import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.Timer;

import com.pet.animation.AnimationManager;
import com.pet.animation.enviroment.NativeWindow;
import com.pet.animation.enviroment.WindowsScanner;
import com.pet.animation.physics.DesktopPhysics;
import java.awt.Rectangle;

public class MouseChasingBehavior {
    private final DesktopPhysics physics;
    private final AnimationManager animationManager;
    private Point lastMousePosition;

    private boolean isChasing = false;

    private Timer chaseUpdateTimer;

    private static final int CHASE_RANGE = 5;
    private static final float CHASE_SPEED = 0.5f;
    private static final int CATCH_RANGE = 0;

    public MouseChasingBehavior (DesktopPhysics physics, AnimationManager animationManager) {
        this.physics = physics;
        this.animationManager = animationManager;

        setupMouseTracking();
    }

    private void setupMouseTracking() {
        Timer mouseTrackingTimer = new Timer(100, e -> {
            lastMousePosition = MouseInfo.getPointerInfo().getLocation();
            // check should chase -> stop or start chasing -> in start update chase timer
            checkShouldChase();
        });
        mouseTrackingTimer.start();
    }

    private void checkShouldChase() {
        if ( lastMousePosition == null) return;

        Point petPosition = physics.getPosition();
        float distanceX = lastMousePosition.x - petPosition.x + 64;

        if(!isChasing && distanceX > CHASE_RANGE && animationManager.getCurrentState().equals(AnimationManager.PLAYING)) {
            startChasing();
        }

        // if(isChasing) {
        //     stopChasing();
        // }
    }

    private void startChasing() {
        isChasing = true;
        System.out.println("MouseChasingBehavior :- Started chasing mouse");

        // start chase update timer
        if (chaseUpdateTimer != null) {
            chaseUpdateTimer.stop();
        }

        chaseUpdateTimer = new Timer(100, e -> updateChase());
        chaseUpdateTimer.start();
    }

    private void updateChase() {
        // if(!isChasing || lastMousePosition == null) return;

        Point petPosition = physics.getPosition();
        int mouseX  = lastMousePosition.x - 120;

        int direactionX = (mouseX > petPosition.x) ? 1 : -1;
        int distanceX = Math.abs(mouseX - petPosition.x);
        System.out.println("MouseChasingBehavior :- PetPosX: " + petPosition.x + ", MouseX: " + mouseX + ", DistanceX: " + distanceX);

        if (distanceX > CATCH_RANGE) {
            float forceX = direactionX * CHASE_SPEED;
            physics.applyForce(forceX, 0);
            System.out.printf("Chasing: Distance=%d, Force=%.1f%n", distanceX, forceX);
        } else {
            if (physics.canJump()) {
                // scan expanded area under the pet to know what is behind current application
                Rectangle expanded = physics.getExpandedBounds();
                java.util.List<NativeWindow> windows = WindowsScanner.getWindowsUnder(expanded);
                System.out.println("MouseChasingBehavior :- Scanned area " + expanded + ", found " + windows.size() + " windows");
                boolean hasAppWindow = false;
                boolean hasDesktopOrTaskbar = false;
                for (NativeWindow w : windows) {
                    System.out.println("  -> " + w);
                    if (w.getType() == NativeWindow.WindowType.APP) hasAppWindow = true;
                    if (w.getType() == NativeWindow.WindowType.DESKTOP || w.getType() == NativeWindow.WindowType.TASKBAR) hasDesktopOrTaskbar = true;
                }

                boolean allowJump;
                if (windows.isEmpty()) {
                    // assume desktop if nothing visible
                    allowJump = true;
                } else {
                    // allow jump only if there are no app windows beneath and at least desktop/taskbar present
                    allowJump = !hasAppWindow && hasDesktopOrTaskbar;
                }

                if (allowJump) {
                    physics.applyForce(0.00f, -8f);
                    System.out.println("JUMP! Caught the mouse! (allowed by environmental scan)");

                    // Celebrate for a moment, then stop chasing
                    Timer celebrateTimer = new Timer(1000, ev -> {
                        isChasing = false;
                        System.out.println("MouseChasingBehavior :- Stopped chasing mouse");

                        if (chaseUpdateTimer != null) {
                            chaseUpdateTimer.stop();
                        }

                        animationManager.setAnimationState(AnimationManager.IDLE);
                    });
                    celebrateTimer.setRepeats(false);
                    celebrateTimer.start();
                } else {
                    System.out.println("MouseChasingBehavior :- Jump skipped, app window present under the pet.");
                }
            } else {
                System.out.println("MouseChasingBehavior :- Jump skipped, pet is not near a screen edge.");
            }
        }
    }

    public void dispose() {
        if (chaseUpdateTimer != null) {
            chaseUpdateTimer.stop();
        }
    }
}
