package com.wgdc.platformer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.runtime.font.FreeTypeFontProvider;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;

public class Assets implements Disposable {

    private static Assets instance;

    private VisAssetManager manager;
    private SpriteBatch batch;
    private String lastScene;

    private Assets() {
        batch = new SpriteBatch();
        manager = new VisAssetManager(batch);
        manager.enableFreeType(new FreeTypeFontProvider());
    }

    public static void create() {
        if(instance != null) {
            instance.dispose();
        }
        instance = new Assets();
    }

    public static Assets instance() {
        return instance;
    }

    public <T> T get(String path, Class<T> type) {
        manager.finishLoading();
        return manager.get(path, type);
    }

    public Scene loadScene(String scenePath, SceneLoader.SceneParameter param) {
        if(lastScene != null) {
            //manager.unload(lastScene);
        }
        lastScene = scenePath;
        return manager.loadSceneNow(scenePath, param);
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
    }
}