package com.wgdc.platformer.screen;

import box2dLight.RayHandler;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.scene.*;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.EntityEngineConfiguration;
import com.wgdc.platformer.Assets;
import com.wgdc.platformer.Platformer;
import com.wgdc.platformer.component.Bullet;
import com.wgdc.platformer.component.Health;
import com.wgdc.platformer.manager.GameManager;
import com.wgdc.platformer.system.*;

public class GameScreen extends ScreenAdapter {

    private Platformer game;
    private Scene scene;
    private RayHandler rayHandler;
    public static OrthographicCamera camera;
    private ShapeRenderer renderer;

    private Body playerBody;
    private Transform playerTransform;

    public GameScreen(Platformer game) {
        this.game = game;
    }

    @Override
    public void show() {
        SceneLoader.SceneParameter param = new SceneLoader.SceneParameter();
        param.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new GameManager(game);
            }
        });

        final LightSystem lightSystem = new LightSystem();
        param.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return lightSystem;
            }
        });

        final ShooterAiSystem shooterAiSystem = new ShooterAiSystem();
        param.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return shooterAiSystem;
            }
        }, SceneConfig.Priority.LOWEST); // TODO: set the width and height of the level from here
        param.config.addSystem(PlayerSystem.class);
        param.config.addSystem(BulletSystem.class);
        param.config.addSystem(LifetimeSystem.class);
        param.config.addSystem(EnemySpawnerSystem.class);
        param.config.addSystem(AnimationSystem.class, SceneConfig.Priority.HIGH);
        param.config.addSystem(DirectedSpriteSystem.class, SceneConfig.Priority.LOWEST);
        param.config.addSystem(DamageSystem.class, SceneConfig.Priority.LOWEST);
        param.config.addSystem(JumpStateSystem.class);
        param.config.addSystem(DeathSystem.class);
        param.config.addSystem(BodyCleanupSystem.class);
        param.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
        Assets.instance().unload("scene/level.scene");
        scene = Assets.instance().loadScene("scene/level.scene", param);

        camera = scene.getEntityEngine().getSystem(CameraManager.class).getCamera();

        World world = scene.getEntityEngine().getSystem(PhysicsSystem.class).getPhysicsWorld();
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Entity e1 = (Entity) contact.getFixtureA().getBody().getUserData();
                Entity e2 = (Entity) contact.getFixtureB().getBody().getUserData();

                Bullet bullet1 = e1.getComponent(Bullet.class);
                if(bullet1 != null) {
                    Health health = e2.getComponent(Health.class);
                    if(health != null) {
                        health.damage(bullet1.damage);
                        e1.deleteFromWorld();
                    }
                    return;
                }

                Bullet bullet2 = e2.getComponent(Bullet.class);
                if(bullet2 != null) {
                    Health health = e1.getComponent(Health.class);
                    if(health != null) {
                        health.damage(bullet2.damage);
                        e2.deleteFromWorld();
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });

        RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.1f);
        lightSystem.setRayHandler(rayHandler);

        renderer = new ShapeRenderer();
        playerBody = shooterAiSystem.player.getComponent(PhysicsBody.class).body;
        playerTransform = shooterAiSystem.player.getComponent(Transform.class);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scene.render();
        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.ORANGE);
        renderer.rect(playerBody.getPosition().x, playerBody.getPosition().y, 0.5f, 0.5f);
        renderer.setColor(Color.CYAN);
        renderer.rect(playerTransform.getX() + 6f, playerTransform.getY() + 6f, 0.5f, 0.5f);
        renderer.end();
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
        rayHandler.dispose();
    }
}
