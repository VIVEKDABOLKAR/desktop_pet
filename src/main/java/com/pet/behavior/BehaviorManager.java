package com.pet.behavior;

import com.pet.animation.physics.DesktopPhysics;
import com.pet.behavior.MouseChasingBehavior;
import com.pet.behavior.PetBehavior;
import com.pet.core.PetContext;
import com.pet.core.constant.PetState;

public class BehaviorManager {

    private final PetContext context;
    private final DesktopPhysics physics;

    private PetBehavior activeBehavior;

    public BehaviorManager(
            PetContext context,
            DesktopPhysics physics
    ) {
        this.context = context;
        this.physics = physics;

        context.addStateListener(this::onStateChanged);
        //FIXED :- handle init pet state
        onStateChanged(context.getState());
    }

    private void onStateChanged(PetState state) {

        stopCurrentBehavior();

        switch (state) {

            case PLAYING:
                startPlayingBehavior();
                break;

            case WALKING:
                startWalkingBehavior();
                break;

            case SITTING:
                startSittingBehavior();
                break;

            case IDLE:
                break;
        }
    }

    private void startPlayingBehavior() {

        activeBehavior = new MouseChasingBehavior(
                context,
                physics
        );

        activeBehavior.start();
    }

    private void startWalkingBehavior() {}

    private void startSittingBehavior() {}

    private void stopCurrentBehavior() {

        if (activeBehavior != null) {
            activeBehavior.stop();
            activeBehavior = null;
        }
    }

    public void dispose() {
        stopCurrentBehavior();
    }
}