package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.wgdc.platformer.component.AnimationState;
import com.wgdc.platformer.component.Health;

public class DamageSystem extends EntityProcessingSystem {

    private ComponentMapper<Health> healthCm;
    private ComponentMapper<AnimationState> animStateCm;

    public DamageSystem() {
        super(Aspect.all(Health.class, AnimationState.class));
    }

    @Override
    protected void process(Entity e) {
        Health h = healthCm.get(e);
        AnimationState a = animStateCm.get(e);

        if(h.hurt) {
            a.setState(AnimationState.State.HURT, Animation.PlayMode.NORMAL, 1f);
            h.hurt = false;
        }
    }
}
