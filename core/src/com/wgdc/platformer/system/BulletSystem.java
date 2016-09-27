package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.PhysicsSprite;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.wgdc.platformer.component.Bullet;

public class BulletSystem extends EntityProcessingSystem {

    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private World physWorld;

    public BulletSystem() {
        super(Aspect.all(Bullet.class));
    }

    @Override
    protected void begin() {
        physWorld = world.getSystem(PhysicsSystem.class).getPhysicsWorld();
    }

    @Override
    public void inserted(Entity e) {
        Transform transform = transformCm.get(e);
        Vector2 worldPos = new Vector2(transform.getX(), transform.getY());

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(worldPos);

        Body body = physWorld.createBody(bodyDef);
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setUserData(e);

        body.setGravityScale(0f);
        body.setBullet(true);
        body.setFixedRotation(true);
        body.setSleepingAllowed(true);
        body.setActive(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        FixtureDef fd = new FixtureDef();
        fd.density = 0.5f;
        fd.friction = 0f;
        fd.restitution = 1f;
        fd.shape = shape;
        fd.filter.groupIndex = -1;

        body.createFixture(fd);
        shape.dispose();

        e.edit().add(new PhysicsBody(body))
                .add(new PhysicsSprite(transform.getRotation()));

        Bullet bullet = e.getComponent(Bullet.class);
        e.getComponent(PhysicsBody.class).body.applyForceToCenter(bullet.velX, bullet.velY, true);
    }

    @Override
    public void removed(Entity e) {
        physWorld.destroyBody(physicsCm.get(e).body);
    }

    @Override
    protected void process(Entity e) {
        Transform t = transformCm.get(e);

        if(t.getX() > 200 || t.getX() < -200 || t.getY() > 200 || t.getY() < -200) {
            e.deleteFromWorld();
        }
    }
}
