package com.wgdc.platformer.manager;

import com.wgdc.platformer.Platformer;
import com.wgdc.platformer.screen.GameScreen;

public class MainMenuManager extends BaseManager {

    public MainMenuManager(Platformer game) {
        super(game);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.setScreenWithTransition(new GameScreen(game), 1f);
        return true;
    }
}
