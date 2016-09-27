package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.wgdc.platformer.component.AnimationState;
import com.wgdc.platformer.component.JumpState;

public class JumpStateSystem extends EntityProcessingSystem {

    private ComponentMapper<JumpState> jumpStateCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<AnimationState> animCm;

    public JumpStateSystem() {
        super(Aspect.all(JumpState.class, PhysicsBody.class, AnimationState.class));
    }

    @Override
    protected void process(Entity e) {
        PhysicsBody body = physicsCm.get(e);
        JumpState state = jumpStateCm.get(e);
        AnimationState animState = animCm.get(e);

        float yVel = body.body.getLinearVelocity().y;
        state.jumping = yVel > 1f || yVel < -1f;

        if(state.jumping && animState.getState() != AnimationState.State.HURT && animState.getState() != AnimationState.State.JUMPING) {
            animState.setState(AnimationState.State.JUMPING);
        }
    }
}
