package com.wgdc.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.wgdc.platformer.screen.MenuScreen;
import com.wgdc.platformer.screen.TransitionScreen;

public class Platformer extends Game {

    @Override
    public void create() {
        Assets.create();
        setScreen(new MenuScreen(this));
    }

    /**
     * Transitions smoothly to the new screen
     * @param screen the screen to show after the transition
     * @param length the length of the transition
     */
    public void setScreenWithTransition(Screen screen, float length) {
        if(screen != null) {
            if(isTransitioning()) {
                ((TransitionScreen) this.screen).finishTransition();
            }
            Gdx.input.setInputProcessor(null);
            this.screen = new TransitionScreen(this, this.screen, screen, length);
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    /**
     * ONLY USE THIS IF YOU'VE ALREADY CALLED SCREEN.SHOW() ON THE SCREEN IN QUESTION
     * @param screen the screen
     */
    public void setScreenWithoutInit(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public boolean isTransitioning() {
        return screen instanceof TransitionScreen;
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.instance().dispose();
    }
}
