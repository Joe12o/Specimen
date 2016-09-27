package com.wgdc.platformer.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.wgdc.platformer.component.ShooterAi;

public class EnemySpawnerSystem extends BaseSystem implements AfterSceneInit {

    private VisIDManager idManager;
    public boolean spawn = true;
    public VisSprite sprite;

    @Override
    public void afterSceneInit() {
        sprite = idManager.get("player").getComponent(VisSprite.class);
    }

    @Override
    protected void processSystem() {
        if(spawn) {
            Entity e = world.createEntity().edit()
                    .add(new Renderable(0))
                    .add(new Layer(1))
                    .add(new Transform(70f, 20f, 0.5f, 0.5f, 0f))
                    .add(sprite)
                    .add(new Origin(-6, -5)).getEntity();

            World physWorld = world.getSystem(PhysicsSystem.class).getPhysicsWorld();
            spawn = false;

            Transform transform = e.getComponent(Transform.class);
            Vector2 worldPos = new Vector2(transform.getX(), transform.getY());

            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(worldPos);

            Body body = physWorld.createBody(bodyDef);
            body.setType(BodyDef.BodyType.DynamicBody);
            body.setUserData(e);

            body.setFixedRotation(true);
            body.setSleepingAllowed(true);
            body.setActive(true);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(2f, 3f);

            FixtureDef fd = new FixtureDef();
            fd.density = 1f;
            fd.friction = 4f;
            fd.restitution = 0f;
            fd.shape = shape;
            fd.filter.groupIndex = -1;

            body.createFixture(fd);
            shape.dispose();

            e.edit().add(new PhysicsBody(body))
                    .add(new PhysicsSprite(transform.getRotation()))
                    .add(new ShooterAi());
        }
    }
}
