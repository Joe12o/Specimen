package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.VisSpriteAnimation;
import com.wgdc.platformer.component.AnimationState;

public class AnimationSystem extends EntityProcessingSystem {

    private ComponentMapper<VisSpriteAnimation> spriteAnimCm;
    private ComponentMapper<AnimationState> animStateCm;

    public AnimationSystem() {
        super(Aspect.all(AnimationState.class, VisSpriteAnimation.class));
    }

    @Override
    protected void process(Entity e) {
        VisSpriteAnimation anim = spriteAnimCm.get(e);
        AnimationState animState = animStateCm.get(e);
        if(animState.dirty) {
            anim.setAnimationName(animState.getState().getAnimName());
            animState.dirty = false;
        }
    }
}
