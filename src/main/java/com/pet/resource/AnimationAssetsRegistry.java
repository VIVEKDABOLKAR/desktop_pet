package com.pet.resource;

import com.pet.animation.AnimationManager;
import com.pet.core.constant.PetState;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load all frames for an animation state
 */
public class AnimationAssetsRegistry {

    /**
     * it store all of the load image based on animal state key
     */
    private final Map<PetState, Image[]> animations;

    private final AssetManager assetManager;

    public AnimationAssetsRegistry(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.animations = new HashMap<>();

        loadAnimationsAssets();
    }

    /**
     * it will load all of the image for all of the animation state
     */
    private void loadAnimationsAssets() {

        registerIdleAnimation();

        registerWalkingAnimation();

        registerSittingAnimation();

        registerPlayingAnimation();
    }

    /**
     * Returns animation frames for a state.
     */
    public Image[] getFrames(PetState state) {
        return animations.get(state);
    }

    /**
     * Returns true if state exists.
     */
    public boolean contains(String state) {
        return animations.containsKey(state);
    }

    /**
     * Registers idle animation.
     */
    private void registerIdleAnimation() {

        animations.put(
                PetState.IDLE,
                new Image[]{
                        assetManager.loadImage("/assets/idle/dog_idle_01.png"),
                        assetManager.loadImage("/assets/idle/dog_idle_02.png"),
                        assetManager.loadImage("/assets/idle/dog_idle_03.png"),
                        assetManager.loadImage("/assets/idle/dog_idle_04.png")
                }
        );
    }

    /**
     * Registers walking animation.
     */
    private void registerWalkingAnimation() {

        List<Image> frames = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {

            for (int j = 1; j <= 3; j++) {

                if ((i == 3 && j == 1)
                        || (i == 3 && j == 2)
                        || (i == 4 && j == 2)
                        || (i == 4 && j == 3)) {

                    continue;
                }

                frames.add(
                        assetManager.loadImage(
                                "/assets/walking/walk_temp/unnamed_"
                                        + i + "_"
                                        + j
                                        + "-Photoroom.png"
                        )
                );
            }
        }

        animations.put(
                PetState.WALKING,
                frames.toArray(new Image[0])
        );
    }

    /**
     * Registers sitting animation.
     */
    private void registerSittingAnimation() {

        animations.put(
                PetState.SITTING,
                new Image[]{
                        assetManager.loadImage("/assets/dog_idle_1.png")
                }
        );
    }

    /**
     * Registers playing animation.
     */
    private void registerPlayingAnimation() {

        animations.put(
                PetState.PLAYING,
                animations.get(PetState.WALKING)
        );
    }

}
