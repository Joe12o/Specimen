package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.kotcrab.vis.runtime.component.VisSpriteAnimation;
import com.wgdc.platformer.component.AnimationState;

public class AnimationSystem extends EntityProcessingSystem {

    private ComponentMapper<VisSpriteAnimation> spriteAnimCm;
    private ComponentMapper<AnimationState> animStateCm;

    public AnimationSystem() {
        super(Aspect.all(AnimationState.class, VisSpriteAnimation.class));
    }

    @Override
    public void inserted(Entity e) {
        AnimationState animState = animStateCm.get(e);
        VisSpriteAnimation anim = spriteAnimCm.get(e);

        animState.defaultFrameDuration = anim.getFrameDuration();
        animState.setState(animState.getState(), animState.getPlayMode(), anim.getFrameDuration());
    }

    @Override
    protected void process(Entity e) {
        VisSpriteAnimation anim = spriteAnimCm.get(e);
        AnimationState animState = animStateCm.get(e);
        if(animState.dirty) {
            anim.setPlayMode(animState.getPlayMode());
            anim.setFrameDuration(animState.getFrameDuration());
            anim.setAnimationName(animState.getState().getAnimName());
            animState.dirty = false;
        } else if(!anim.isPlaying()) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
            anim.setAnimationName(AnimationState.State.IDLE.getAnimName());
        }
    }
}
