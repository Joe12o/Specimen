package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.VisID;
import com.wgdc.platformer.component.Health;
import com.wgdc.platformer.component.Lifetime;

public class DeathSystem extends EntityProcessingSystem {

    private ComponentMapper<Health> healthCm;
    private ComponentMapper<VisID> idCm;

    public DeathSystem() {
        super(Aspect.all(Health.class).exclude(Lifetime.class));
    }

    @Override
    protected void process(Entity e) {
        Health h = healthCm.get(e);
        VisID id = idCm.get(e);

        if(h.getCurrent() <= 0 && (id == null || !id.id.equals("player"))) {
            e.edit().add(new Lifetime(0));
        }
    }
}
