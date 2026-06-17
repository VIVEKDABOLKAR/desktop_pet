package com.pet.core;

import com.pet.core.constant.PetState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PetContext {

    /**
     * current state of the pet
     */
    private PetState currentState;

    /**
     * define list of consumer package of petState
     */
    private final List<Consumer<PetState>>
            stateListeners;

    public PetContext() {
        this.currentState = PetState.IDLE;

        stateListeners = new ArrayList<>();
    }

    //event system

    /**
     * add manger listener (for petState) to oue consumer listener
     * @return
     */
    public void addStateListener(
            Consumer<PetState> listener
    ) {
        stateListeners.add(listener);
    }

    /**
     * when state change notify to each subscriber
     */
    private void notifyStateListeners() {

        for(Consumer<PetState> listener : stateListeners) {
            listener.accept(
                    currentState
            );
        }
    }

    //getter setter of variable/constant
    public synchronized PetState getState() {
        return currentState;
    }

    public synchronized void setState(
            PetState state
    ) {

        //if same state do nothing return
        if(currentState == state) {
            return;
        }

        this.currentState = state;

        //notify to each subscriber
        notifyStateListeners();
    }
}