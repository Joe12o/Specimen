package com.wgdc.platformer.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

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
    private Animation.PlayMode playMode;
    private float frameDuration;
    public float defaultFrameDuration;
    public boolean dirty = true;

    public AnimationState() {
        currentState = State.IDLE;
        playMode = Animation.PlayMode.LOOP;
        frameDuration = 0.2f;
    }

    public void setState(State newState) {
        setState(newState, Animation.PlayMode.LOOP, defaultFrameDuration);
    }

    public void setState(State newState, Animation.PlayMode newMode, float newFrameDur) {
        if(currentState != newState || newMode != playMode) {
            currentState = newState;
            playMode = newMode;
            frameDuration = newFrameDur;
            dirty = true;
        }
    }

    public State getState() {
        return currentState;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public float getFrameDuration() {
        return frameDuration;
    }
}
