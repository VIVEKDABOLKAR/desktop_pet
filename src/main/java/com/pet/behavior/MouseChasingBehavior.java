package com.pet.behavior;

import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.Timer;

import com.pet.animation.AnimationManager;
import com.pet.animation.physics.DesktopPhysics;

public class MouseChasingBehavior {
    private DesktopPhysics physics;
    private AnimationManager animationManager;
    private Point lastMousePosition;

    private boolean isChasing = false;

    private Timer mouseTrackingTimer;
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
        mouseTrackingTimer = new Timer(100, e -> {
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

        if(!isChasing && distanceX > CHASE_RANGE && animationManager.getCurrentState().equals(animationManager.PLAYING)) {
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

        chaseUpdateTimer = new Timer(100, e -> {
            updateChase();
        });
        chaseUpdateTimer.start();
    }

    private void updateChase() {
        // if(!isChasing || lastMousePosition == null) return;

        Point petPosition = physics.getPosition();
        int mouseX  = lastMousePosition.x - 120;

        int direactionX = (mouseX > petPosition.x) ? 1 : -1;
        int distanceX = Math.abs(mouseX - petPosition.x);
        System.out.println("MouseChasingBehavior :- PetPosX: " + petPosition.x + ", MouseX: " + mouseX + ", DistanceX: " + distanceX);

        int direactionY = 0; // only horizontal chasing for now
        int distanceY = 0;

        if (distanceX > CATCH_RANGE) {
            float forceX = direactionX * CHASE_SPEED;
            physics.applyForce(forceX, 0);
            System.out.printf("Chasing: Distance=%d, Force=%.1f%n", distanceX, forceX);
        } else {
            if (physics.isOnGround()) {
                physics.applyForce(0.00f, -8f);
                System.out.println("JUMP! Caught the mouse!");
                
                // Celebrate for a moment, then stop chasing
                Timer celebrateTimer = new Timer(1000, ev -> {
                    // stopChasing();
                });
                celebrateTimer.setRepeats(false);
                celebrateTimer.start();
            }
        }
    }

    private void stopChasing() {
        isChasing = false;
        System.out.println("MouseChasingBehavior :- Stopped chasing mouse");

        if (chaseUpdateTimer != null) {
            chaseUpdateTimer.stop();
        }

        animationManager.setAnimationState(animationManager.IDLE);
    }

    public void dispose() {
        if (chaseUpdateTimer != null) {
            chaseUpdateTimer.stop();
        }
    }
}
