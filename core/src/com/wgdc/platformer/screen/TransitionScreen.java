package com.wgdc.platformer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.wgdc.platformer.Platformer;

public class TransitionScreen extends ScreenAdapter {

    private enum AnimDirection { LEFT, RIGHT, UP, DOWN }

    private SpriteBatch batch;
    private FrameBuffer fbo;
    private TextureRegion fboRegion;
    private Platformer game;
    private Screen oldScreen;
    private Screen newScreen;
    private AnimDirection animDirection;
    private float length;
    private float timeShown = 0f;

    public TransitionScreen(Platformer game, Screen oldScreen, Screen newScreen, float length) {
        this.game = game;
        this.oldScreen = oldScreen;
        this.newScreen = newScreen;
        this.length = length;
        this.animDirection = AnimDirection.values()[MathUtils.random(AnimDirection.values().length - 1)];
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        if (newScreen != null) {
            newScreen.show();
        }

        try {
            // TODO: Use an fbo with a PoT size to avoid incompatibilities
            fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, fbo.getWidth(), fbo.getHeight());
            fboRegion.flip(false, true);
        } catch(GdxRuntimeException e) {
            game.setScreenWithoutInit(newScreen);
        }
    }

    @Override
    public void render(float delta) {
        if(timeShown >= length) {
            finishTransition();
            return;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        newScreen.render(delta);

        fbo.begin();
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        oldScreen.render(delta);
        fbo.end();

        batch.begin();
        batch.setColor(1f, 1f, 1f, MathUtils.lerp(1f, 0f, timeShown / length));
        switch(animDirection) {
            case LEFT: batch.draw(fboRegion, MathUtils.lerp(0f, -Gdx.graphics.getWidth(), timeShown / length), 0f);
                break;
            case RIGHT: batch.draw(fboRegion, MathUtils.lerp(0f, Gdx.graphics.getWidth(), timeShown / length), 0f);
                break;
            case UP: batch.draw(fboRegion, 0f, MathUtils.lerp(0f, Gdx.graphics.getHeight(), timeShown / length));
                break;
            case DOWN: batch.draw(fboRegion, 0f, MathUtils.lerp(0f, -Gdx.graphics.getHeight(), timeShown / length));
                break;
        }
        batch.end();

        timeShown += delta;
    }

    public void finishTransition() {
        game.setScreenWithoutInit(newScreen);
    }

    @Override
    public void resize (int width, int height) {
        fbo.dispose();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        fboRegion.setTexture(fbo.getColorBufferTexture());
        fboRegion.setRegion(0, 0, fbo.getWidth(), fbo.getHeight());
        fboRegion.flip(false, true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        fbo.dispose();
        batch.dispose();
    }
}