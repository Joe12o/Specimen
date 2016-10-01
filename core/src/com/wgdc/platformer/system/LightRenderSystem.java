package com.wgdc.platformer.system;

import box2dLight.RayHandler;
import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

public class LightRenderSystem extends BaseSystem implements AfterSceneInit {

    private CameraManager camManager;
    private RayHandler rayHandler;
    private OrthographicCamera cam;

    @Override
    public void afterSceneInit() {
        cam = camManager.getCamera();
    }

    @Override
    protected void processSystem() {
        rayHandler.setCombinedMatrix(cam);
        rayHandler.updateAndRender();
    }

    public void setRayHandler(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }
}
