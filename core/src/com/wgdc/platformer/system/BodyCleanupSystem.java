package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.PhysicsProperties;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;

public class BodyCleanupSystem extends EntityProcessingSystem {

    private ComponentMapper<PhysicsBody> physicsCm;
    private World physWorld;

    public BodyCleanupSystem() {
        super(Aspect.all(PhysicsBody.class).exclude(PhysicsProperties.class));
    }

    @Override
    protected void begin() {
        physWorld = world.getSystem(PhysicsSystem.class).getPhysicsWorld();
    }

    @Override
    public void removed(Entity e) {
        physWorld.destroyBody(physicsCm.get(e).body);
    }

    @Override
    protected void process(Entity e) {}
}
