package com.wgdc.platformer.ai.fsm;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.kotcrab.vis.runtime.component.*;
import com.wgdc.platformer.component.Bullet;
import com.wgdc.platformer.component.Lifetime;
import com.wgdc.platformer.component.Light;
import com.wgdc.platformer.screen.GameScreen;
import com.wgdc.platformer.system.PlayerSystem;

public enum ShooterState implements State<ShooterAgent> {
    PURSUE() {
        @Override
        public void update(ShooterAgent entity) {
            if(entity.playerEntity == null) {
                entity.stateMachine.changeState(IDLE);
            }

            if(canSeePlayer(entity)) {
                entity.stateMachine.changeState(ATTACK);
                return;
            }

            if(entity.nextNode == null || entity.ticks > 0) {
                entity.nextNode = entity.pathfinder.findNextNode(entity.getPosition().sub(0f, 4f), entity.getPlayerPosition().add(6f, 6f));
                entity.ticks = 0;
            }

            // We didn't find a path, idle
            if(entity.nextNode == null) {
                entity.stateMachine.changeState(IDLE);
                return;
            }

            if(entity.shooterBody.getLinearVelocity().x > 25f || entity.shooterBody.getLinearVelocity().x < -25f) return;
            vec1.set(entity.nextNode.worldX, 0f);
            vec2.set(entity.shooterBody.getWorldCenter().x, 0f);
            entity.shooterBody.applyLinearImpulse(vec1.sub(vec2).nor().scl(180f), entity.shooterBody.getWorldCenter(), true);
        }
    },
    ATTACK() {
        @Override
        public void update(ShooterAgent entity) {
            if(!canSeePlayer(entity)) {
                entity.stateMachine.changeState(PURSUE);
                return;
            }

            renderer.setProjectionMatrix(GameScreen.camera.combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.YELLOW);
            renderer.rect(entity.getPosition().x, entity.getPosition().y, 2f, 2f);
            renderer.end();

            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.ORANGE);
            renderer.line(entity.getPosition(), entity.getPlayerPosition().add(6f, 10f));
            renderer.end();


            if(shotCooldown <= 0) {
                vec1.set(entity.getPlayerPosition());
                vec2.set(entity.getPosition());
                vec1.add(6f, 10f).sub(vec2).nor().scl(1500f);

                entity.shooterEntity.getWorld().createEntity().edit()
                        .add(new Renderable(0))
                        .add(new Layer(1))
                        .add(new Transform(entity.getPosition().x, entity.getPosition().y, 0.15f, 0.15f, 0f))
                        .add(PlayerSystem.bulletSprite)
                        .add(new Bullet(vec1.x, vec1.y))
                        .add(new Light(8, Color.GREEN, 10f, 0f, 0f))
                        .add(new Origin(-1.1f, -1.15f))
                        .add(new Lifetime(3))
                        .add(new VisID("bullet"));

                shotCooldown = 20;
            }

            shotCooldown--;
        }
    }, IDLE() {
        @Override
        public void update(ShooterAgent entity) {
            if(entity.playerEntity != null) {
                entity.stateMachine.changeState(PURSUE);
            }
        }
    };

    private static final Vector2 vec1 = new Vector2();
    private static final Vector2 vec2 = new Vector2();
    private static boolean playerVisible = false;
    private static int shotCooldown = 0;
    private static ShapeRenderer renderer = new ShapeRenderer();

    private static final RayCastCallback raycastCallback = new RayCastCallback() {
        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            Entity e = (Entity) fixture.getBody().getUserData();
            VisID id = e.getComponent(VisID.class);
            if(id != null) {
                if(!id.id.equals("player") && !id.id.equals("bullet") && !id.id.equals("enemy")) {
                    playerVisible = false;
                    return 0;
                }
            } else {
                playerVisible = false;
                return 0;
            }
            return 1;
        }
    };

    public boolean canSeePlayer(ShooterAgent entity) {
        playerVisible = true;
        entity.shooterBody.getWorld().rayCast(raycastCallback, entity.getPosition(), entity.getPlayerPosition().add(6f, 10f));
        return playerVisible;
    }

    @Override
    public void enter(ShooterAgent entity) {
        entity.ticks = 0;
    }

    @Override
    public void exit(ShooterAgent entity) {
        entity.nextNode = null;
    }

    @Override
    public boolean onMessage(ShooterAgent entity, Telegram telegram) {
        return false;
    }
}
