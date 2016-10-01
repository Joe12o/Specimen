package com.wgdc.platformer.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.wgdc.platformer.component.Health;

public class HealthBarSystem extends BaseSystem implements AfterSceneInit {

    private CameraManager camManager;
    private VisIDManager idManager;

    private Health playerHealth;
    private OrthographicCamera cam;
    private ShapeRenderer sRenderer;

    private final Color color = new Color(0.8f, 0.8f, 0.8f, 0.5f);

    @Override
    public void afterSceneInit() {
        Entity player = idManager.get("player");
        playerHealth = player.getComponent(Health.class);
        cam = camManager.getCamera();
        sRenderer = new ShapeRenderer();
    }

    @Override
    protected void processSystem() {
        sRenderer.setProjectionMatrix(cam.combined);
        sRenderer.begin(ShapeRenderer.ShapeType.Filled);
        sRenderer.setColor(color);
        sRenderer.rectLine(1, 70, 20, 70, 0.3f);
        sRenderer.rectLine(1, 68, 20, 68, 0.3f);
        sRenderer.rectLine(1, 70, 1, 68, 0.3f);
        sRenderer.rectLine(20, 70, 20, 68, 0.3f);
        sRenderer.rect(1.3f, 68.3f, 18.4f * ((float)playerHealth.getCurrent() / (float)playerHealth.max), 1.4f);
        sRenderer.end();
    }

    @Override
    protected void dispose() {
        sRenderer.dispose();
        System.out.println("disposed");
    }
}
