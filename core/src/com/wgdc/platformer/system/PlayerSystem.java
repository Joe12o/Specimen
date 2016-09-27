package com.wgdc.platformer.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.wgdc.platformer.component.*;
import com.wgdc.platformer.manager.GameManager;

public class PlayerSystem extends BaseSystem implements AfterSceneInit {

    private static final float WALK_SPEED = 30f;

    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<VisSpriteAnimation> animationCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private VisIDManager idManager;
    private GameManager gameManager;
    private CameraManager camManager;

    private Entity player;
    private VisSprite sprite;
    private VisSpriteAnimation animation;
    private PhysicsBody body;
    private Transform transform;
    private JumpState jumpState;
    private AnimationState animState;
    private Health health;
    private Facing facing;
    private int shotCooldown = 0;
    public static VisSprite bulletSprite;

    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        player.edit().add(jumpState = new JumpState())
                .add(new Light(40, Color.YELLOW, 15f, 5f, 10f))
                .add(new Health(20))
                .add(new AnimationState()).add(new Facing(Facing.Direction.RIGHT));
        sprite = spriteCm.get(player);
        animation = animationCm.get(player);
        transform = transformCm.get(player);
        body = physicsCm.get(player);
        health = player.getComponent(Health.class);
        animState = player.getComponent(AnimationState.class);
        facing = player.getComponent(Facing.class);

        EdgeShape shape = new EdgeShape();
        shape.set(3.5f, 14f, 3.5f, 6.45f);

        FixtureDef fd = new FixtureDef();
        fd.density = 0f;
        fd.friction = 0f;
        fd.shape = shape;
        fd.filter.groupIndex = -1;

        body.body.createFixture(fd);

        shape.set(9.2f, 14f, 9.2f, 6.45f);
        body.body.createFixture(fd);
        shape.dispose();

        for(Fixture f : body.body.getFixtureList()) {
            Filter filter = f.getFilterData();
            filter.groupIndex = -1;
            f.setFilterData(filter);
        }

        Entity tempBullet = idManager.get("tempBullet");
        bulletSprite = tempBullet.getComponent(VisSprite.class);
        tempBullet.deleteFromWorld();
    }

    @Override
    protected void processSystem() {
        if(health.current <= 0) {
            gameManager.state = GameManager.State.GAME_OVER;
        }

        if(gameManager.state == GameManager.State.RUNNING) {
            if(Gdx.input.isKeyPressed(Input.Keys.A)) {
                if(body.body.getLinearVelocity().x > -WALK_SPEED) {
                    body.body.applyLinearImpulse(-1000f, 0f, 0f, 0f, true);
                }
                facing.direction = Facing.Direction.LEFT;
                animState.setState(AnimationState.State.WALKING);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.D)) {
                if(body.body.getLinearVelocity().x < WALK_SPEED) {
                    body.body.applyLinearImpulse(1000f, 0f, 0f, 0f, true);
                }
                facing.direction = Facing.Direction.RIGHT;
                animState.setState(AnimationState.State.WALKING);
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.W) && !jumpState.jumping) {
                body.body.applyLinearImpulse(0f, 3500f, 0f, 0f, true);
                jumpState.jumping = true;
                animState.setState(AnimationState.State.JUMPING);
            }

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shotCooldown <= 0) {
                Vector3 velocity = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f);
                camManager.getCamera().unproject(velocity);
                velocity.sub(transform.getX() + 6.2f, transform.getY() + 10f, 0);
                velocity.nor();
                velocity.scl(1800f, 1800f, 0f);
                world.createEntity().edit()
                        .add(new Renderable(0))
                        .add(new Layer(1))
                        .add(new Transform(transform.getX() + 6.2f, transform.getY() + 10f, 0.15f, 0.15f, 0f))
                        .add(bulletSprite)
                        .add(new Bullet(velocity.x, velocity.y))
                        .add(new Light(8, Color.RED, 10f, 0f, 0f))
                        .add(new Origin(-1.1f, -1.15f))
                        .add(new Lifetime(3))
                        .add(new VisID("bullet"));
                shotCooldown = 10;
            }

            if(shotCooldown > 0) {
                shotCooldown--;
            }
        }

        if(Math.abs(body.body.getLinearVelocity().x) < 0.1f && !jumpState.jumping && !animation.getAnimationName().equals("idle")) {
            animState.setState(AnimationState.State.IDLE);
        }
    }
}
