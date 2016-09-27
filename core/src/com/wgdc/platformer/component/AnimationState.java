package com.wgdc.platformer.component;

import com.artemis.Component;

public class AnimationState extends Component {
    public enum State {
        IDLE("idle"),
        WALKING("walk"),
        JUMPING("jump"),
        HURT("hurt");

        private String animName;

        State(String animName) {
            this.animName = animName;
        }

        public String getAnimName() {
            return animName;
        }
    }

    private State currentState;
    public boolean dirty = true;

    public AnimationState() {
        currentState = State.IDLE;
    }

    public void setState(State newState) {
        if(currentState != newState) {
            currentState = newState;
            dirty = true;
        }
    }

    public State getState() {
        return currentState;
    }
}
