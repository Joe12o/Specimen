package com.wgdc.platformer.screen;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.SystemProvider;
import com.kotcrab.vis.runtime.util.EntityEngineConfiguration;
import com.wgdc.platformer.Assets;
import com.wgdc.platformer.Platformer;
import com.wgdc.platformer.manager.MainMenuManager;

public class MenuScreen extends ScreenAdapter {

    private Platformer game;
    private Scene scene;

    public MenuScreen(Platformer game) {
        this.game = game;
    }

    @Override
    public void show() {
        SceneLoader.SceneParameter param = new SceneLoader.SceneParameter();
        param.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new MainMenuManager(game);
            }
        });

        scene = Assets.instance().loadScene("scene/main_menu.scene", param);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scene.render();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
    }
}
