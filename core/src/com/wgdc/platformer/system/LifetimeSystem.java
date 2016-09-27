package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.wgdc.platformer.component.Lifetime;

public class LifetimeSystem extends EntityProcessingSystem {

    private ComponentMapper<Lifetime> lifetimeCm;

    public LifetimeSystem() {
        super(Aspect.all(Lifetime.class));
    }

    @Override
    protected void process(Entity e) {
        Lifetime lifetime = lifetimeCm.get(e);

        if(System.currentTimeMillis() - lifetime.birthTime >= lifetime.lifetime * 1000) {
            e.deleteFromWorld();
        }
    }
}
