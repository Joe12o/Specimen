package com.wgdc.platformer.manager;

import com.artemis.Entity;
import com.kotcrab.vis.runtime.component.Invisible;
import com.wgdc.platformer.Platformer;
import com.wgdc.platformer.screen.MenuScreen;

public class GameManager extends BaseManager {

    public enum State { GET_READY, RUNNING, GAME_OVER }

    private State state = State.GET_READY;

    private Entity gameOverTextEntity;
    private Entity gameOverSubtextEntity;

    public GameManager(Platformer game) {
        super(game);
    }

    @Override
    public void afterSceneInit() {
        super.afterSceneInit();
        gameOverTextEntity = idManager.get("gameOver");
        gameOverSubtextEntity = idManager.get("gameOverSubtext");

        gameOverTextEntity.edit().add(new Invisible());
        gameOverSubtextEntity.edit().add(new Invisible());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(state == State.GET_READY) {
            state = State.RUNNING;
        } else if(state == State.GAME_OVER) {
            game.setScreenWithTransition(new MenuScreen(game), 1f);
        }
        return true;
    }

    public void setState(State newState) {
        this.state = newState;
        if(state == State.GAME_OVER) {
            gameOverTextEntity.edit().remove(Invisible.class);
            gameOverSubtextEntity.edit().remove(Invisible.class);
            // gameOverTextEntity.edit().add(new Light(4, Color.WHITE, 50, 20, 10));
        }
    }

    public State getState() {
        return state;
    }
}
