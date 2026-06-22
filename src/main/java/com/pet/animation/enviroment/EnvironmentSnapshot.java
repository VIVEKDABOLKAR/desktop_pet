package com.pet.animation.enviroment;

import com.pet.animation.enviroment.NativeWindow;

/**
 * Represents a snapshot of the pet's environment at a given moment.
 * It contains information about the pet's current state in relation to its surroundings.
 */
public class EnvironmentSnapshot {

    private boolean onDesktop;
    private boolean onWindow;
    private boolean onTaskbar;

    private boolean gapAhead;
    private boolean obstacleAhead;

    private NativeWindow currentSurface;

    public boolean isOnDesktop() {
        return onDesktop;
    }

    public void setOnDesktop(boolean onDesktop) {
        this.onDesktop = onDesktop;
    }

    public boolean isOnWindow() {
        return onWindow;
    }

    public void setOnWindow(boolean onWindow) {
        this.onWindow = onWindow;
    }

    public boolean isOnTaskbar() {
        return onTaskbar;
    }

    public void setOnTaskbar(boolean onTaskbar) {
        this.onTaskbar = onTaskbar;
    }

    public boolean isGapAhead() {
        return gapAhead;
    }

    public void setGapAhead(boolean gapAhead) {
        this.gapAhead = gapAhead;
    }

    public boolean isObstacleAhead() {
        return obstacleAhead;
    }

    public void setObstacleAhead(boolean obstacleAhead) {
        this.obstacleAhead = obstacleAhead;
    }

    public NativeWindow getCurrentSurface() {
        return currentSurface;
    }

    public void setCurrentSurface(NativeWindow currentSurface) {
        this.currentSurface = currentSurface;
    }
}