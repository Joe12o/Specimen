package com.wgdc.platformer.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.component.*;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.wgdc.platformer.component.*;
import com.wgdc.platformer.manager.GameManager;

public class EnemySpawnerSystem extends BaseSystem implements AfterSceneInit {

    private VisIDManager idManager;
    private GameManager gameManager;
    private Array<Vector2> spawnPoints;
    private int ticksSinceLastSpawn = 0;

    // Robot template stuff
    private AssetReference robotAssetRef;
    private VisSpriteAnimation robotAnimation;
    private VisSprite robotSprite;
    private Transform robotTransform;

    private World physWorld;

    @Override
    public void afterSceneInit() {
        spawnPoints = new Array<Vector2>();
        Array<Entity> pointEntities = idManager.getMultiple("spawnpoint");
        for(Entity e : pointEntities) {
            Transform t = e.getComponent(Transform.class);
            spawnPoints.add(new Vector2(t.getX(), t.getY()));
        }

        Entity template = idManager.get("tempRobot");
        robotAssetRef = template.getComponent(AssetReference.class);
        robotAnimation = template.getComponent(VisSpriteAnimation.class);
        robotSprite = template.getComponent(VisSprite.class);
        robotTransform = template.getComponent(Transform.class);
        template.deleteFromWorld();

        physWorld = world.getSystem(PhysicsSystem.class).getPhysicsWorld();
    }

    @Override
    protected void processSystem() {
        if(MathUtils.random(50) == 1 && ticksSinceLastSpawn >= 180 && gameManager.getState() == GameManager.State.RUNNING) {
            Vector2 spawnLocation = spawnPoints.random();
            Body body = getNewBody(spawnLocation);
            VisSpriteAnimation animation = new VisSpriteAnimation(robotAnimation);
            animation.setDirty(true);
            Entity e = world.createEntity().edit()
                    .add(new Renderable(0))
                    .add(new Layer(1))
                    .add(new Transform(spawnLocation.x, spawnLocation.y, robotTransform.getScaleX(), robotTransform.getScaleY(), 0f))
                    .add(new VisSprite(robotSprite))
                    .add(new VisSpriteAnimation())
                    .add(robotAssetRef)
                    .add(new Tint())
                    .add(new Origin(-3.75f, -2.75f))
                    .add(new Facing(Facing.Direction.RIGHT))
                    .add(new AutoDirection())
                    .add(new PhysicsBody(body))
                    .add(new PhysicsSprite(0f))
                    .add(new AnimationState())
                    .add(new Health(10))
                    .add(new ShooterAi()).getEntity();
            body.setUserData(e);
            ticksSinceLastSpawn = 0;
        }
        ticksSinceLastSpawn++;
    }

    private Body getNewBody(Vector2 location) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(location);

        Body body = physWorld.createBody(bodyDef);
        body.setType(BodyDef.BodyType.DynamicBody);

        body.setFixedRotation(true);
        body.setSleepingAllowed(true);
        body.setActive(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2.7f, 1.85f);

        FixtureDef fd = new FixtureDef();
        fd.friction = 4f;
        fd.shape = shape;
        fd.filter.categoryBits = 0x0004;
        fd.filter.maskBits = 0x0001 | 0x0010; // GROUND | PLAYER_BULLETS
        //fd.filter.groupIndex = -2;

        body.createFixture(fd);
        shape.dispose();

        return body;
    }
}
