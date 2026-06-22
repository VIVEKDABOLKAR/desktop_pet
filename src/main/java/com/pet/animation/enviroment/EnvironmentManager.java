package com.pet.animation.enviroment;

import com.pet.animation.physics.PhysicsEngine;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EnvironmentManager {

    private final PhysicsEngine physics;
    private final EnvironmentSnapshot snapshot;

    private Timer updateTimer;

    public EnvironmentManager(
            PhysicsEngine physics
    ) {
        this.physics = physics;
        this.snapshot = new EnvironmentSnapshot();

        startUpdateLoop();
    }

    private void startUpdateLoop() {

        updateTimer = new Timer();

        updateTimer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        scanEnvironment();
                    }
                },
                0,
                2000
        );
    }

    private void scanEnvironment() {

        // next step
        System.out.println("ENVIRONMENT_MANAGER :- Scanning environment...");

        Rectangle area = physics.getExpandedBounds();

        List<NativeWindow> windows =
                WindowsScanner.getWindowsUnder(area);

        boolean hasAppWindow = false;
        boolean hasDesktop = false;
        boolean hasTaskbar = false;

        for (NativeWindow window : windows) {
            System.out.println("Type of window :- " + window.getType());

            switch (window.getType()) {

                case APP:
                    hasAppWindow = true;
                    break;

                case DESKTOP:
                    hasDesktop = true;
                    break;

                case TASKBAR:
                    hasTaskbar = true;
                    break;
            }
        }

        snapshot.setOnWindow(hasAppWindow);
        snapshot.setOnDesktop(hasDesktop);
        snapshot.setOnTaskbar(hasTaskbar);

        System.out.println("ENVIRONMENT_MANAGER :- Snapshot updated: on desktop " +
                snapshot.isOnDesktop() +
                ", on windows " + snapshot.isOnWindow() +
                ", on taskbar " + snapshot.isOnTaskbar());
    }

    public EnvironmentSnapshot getSnapshot() {
        return snapshot;
    }

    public void dispose() {
        if(updateTimer != null) {
            updateTimer.cancel();
        }
    }
}