package com.wgdc.platformer.manager;

import com.wgdc.platformer.Platformer;

public class GameManager extends BaseManager {

    public enum State { GET_READY, RUNNING, GAME_OVER }

    public State state = State.GET_READY;

    public GameManager(Platformer game) {
        super(game);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(state == State.GET_READY) {
            state = State.RUNNING;
        }
        return true;
    }
}
