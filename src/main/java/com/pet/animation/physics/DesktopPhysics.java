package com.pet.animation.physics;  

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DesktopPhysics {
    // physics parameters
    private static final float GRAVITY = 0.5f; // gravity acceleration
    private static final float MAX_FALL_SPEED = 15f; // max falling speed
    private static final float FRICTION = 0.9f; // friction coefficient
    private static final float JUMP_FORCE = -12f; // bounce damping factor

    // effective vectors 
    private float velocityX = 0;
    private float velocityY = 0;
    private float positionX = 100;
    private float positionY = 100;
    private boolean onGround = false;
    private boolean isJumping = false;

    // //screen bounds for desktop
    private Rectangle screenBounds;

    // //robot for simulating mouse movements
    private Robot robot;


    // Constructor - initialize screen bounds and robot (get info about the desktop)
    public DesktopPhysics() {
        try {
            this.robot = new Robot();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.screenBounds = new Rectangle(0, 0, screenSize.width, screenSize.height);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // apply physicsto the pet's position
    public void update() {
        // apply gravity
        if (!onGround) {
            velocityY += GRAVITY;
            velocityY = Math.min(velocityY, MAX_FALL_SPEED);
        }

        // update position
        positionX += velocityX;
        positionY += velocityY;

        // apply friction
        if (onGround) {
            velocityX *= FRICTION;
            if (Math.abs(velocityX) < 0.1f) {
                velocityX = 0;
            }
        }

        // check ground collision
        if (positionY >= screenBounds.height - 115) { // assuming pet height is 128
            positionY = screenBounds.height - 115;
            velocityY = 0;
            onGround = true;
            isJumping = false;
        } else {
            onGround = false;
        }

        // ensure pet stays within screen bounds
        if (positionX < 0) positionX = 0;
        if (positionX > screenBounds.width - 128) positionX = screenBounds.width - 128; // assuming pet width is 128
    }

    // apply external force to the pet
    public void applyForce(float forceX, float forceY) {
        this.velocityX += forceX;
        this.velocityY += forceY;
    }

    // getter and setter
    public Point getPosition() {
        return new Point((int) positionX, (int) positionY);
    }

    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public Rectangle getScreenBound() {
        return screenBounds;
    }

    public static void main(String[] args) {
        DesktopPhysics physics = new DesktopPhysics();
        System.out.println("Screen Bounds: " + physics.screenBounds);

    }
}
