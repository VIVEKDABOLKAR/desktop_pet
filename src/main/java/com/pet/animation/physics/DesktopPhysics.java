package com.pet.animation.physics;  

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DesktopPhysics {
    // physics parameters
    private static final float GRAVITY = 0.5f; // gravity acceleration
    private static final float MAX_FALL_SPEED = 15f; // max falling speed
    private static final float FRICTION = 0.9f; // friction coefficient
    private static final int EDGE_MARGIN = 20;

    //pet size
    private static final int width = PetBody.WIDTH;
    private static final int height = PetBody.HEIGHT;

    // effective vectors 
    private float velocityX = 0;
    private float velocityY = 0;
    private float positionX = 100;
    private float positionY = 100;
    private boolean onGround = false;

    // //screen bounds for desktop
    private final Rectangle screenBounds;


    // Constructor - initialize screen bounds from the desktop
    public DesktopPhysics() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenBounds = new Rectangle(0, 0, screenSize.width, screenSize.height);
    }

    // apply physics to the pet's position
    public void update() {
        // apply gravity
        if (!onGround) {
            velocityY = Math.min(getVelocityY() + GRAVITY, MAX_FALL_SPEED);
        }

        // update position
        positionX += velocityX;
        positionY += velocityY;

        // apply friction
        if (onGround) {
            velocityX *= FRICTION;
            if (Math.abs(getVelocityX()) < 0.1f) {
                velocityX = 0;
            }
        }

        // check ground collision
        if (positionY >= screenBounds.height - height) {
            resolveGround();
            onGround = true;
        } else {
            onGround = false;
        }

        // ensure pet stays within screen bounds
        if (positionX < 0) positionX = 0;
        if (positionX > screenBounds.width - width) positionX = screenBounds.width - width;
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
        return positionY + height >= screenBounds.height;
    }

    public boolean isNearLeftEdge() {
        return positionX <= EDGE_MARGIN;
    }

    public boolean isNearRightEdge() {
        return positionX + width >= screenBounds.width - EDGE_MARGIN;
    }

    public boolean isNearHorizontalEdge() {
        Rectangle2D expandedPetBounds = new Rectangle2D.Float(
                positionX - EDGE_MARGIN,
                positionY,
                width + (EDGE_MARGIN * 2f),
                height);
        return !screenBounds.contains(expandedPetBounds);
    }

    public boolean canJump() {
        return isOnGround() && (isEdgeAheadLeft() || isEdgeAheadRight());
    }

    public void resolveGround() {
    if (isOnGround()) {
        positionY = screenBounds.height - height;
        velocityY = 0;
    }
}

    public boolean isEdgeAheadRight() {
        return isNearHorizontalEdge() && isNearRightEdge();
    }

    public boolean isEdgeAheadLeft() {
        return isNearHorizontalEdge() && isNearLeftEdge();
    }

    public Rectangle getScreenBound() {
        return screenBounds;
    }

    public int getEdgeMargin() {
        return EDGE_MARGIN;
    }

    public Rectangle getExpandedBounds() {
        int x = Math.max(0, (int) positionX - EDGE_MARGIN);
        int y = Math.max(0, (int) positionY);
        int w = Math.min(screenBounds.width - x, width + EDGE_MARGIN * 2);
        int h = Math.min(screenBounds.height - y, height);
        return new Rectangle(x, y, w, h);
    }

    public static void main(String[] args) {
        DesktopPhysics physics = new DesktopPhysics();
        System.out.println("Screen Bounds: " + physics.screenBounds);

    }
}
