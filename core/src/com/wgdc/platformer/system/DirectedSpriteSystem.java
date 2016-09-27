package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.wgdc.platformer.component.AutoDirection;
import com.wgdc.platformer.component.Facing;

public class DirectedSpriteSystem extends EntityProcessingSystem {

    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<Facing> facingCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<AutoDirection> autoDirCm;

    public DirectedSpriteSystem() {
        super(Aspect.all(Facing.class, VisSprite.class));
    }

    @Override
    protected void process(Entity e) {
        VisSprite sprite = spriteCm.get(e);
        Facing facing = facingCm.get(e);
        AutoDirection autoDir = autoDirCm.get(e);

        if(autoDir != null) {
            PhysicsBody physicsBody = physicsCm.get(e);
            if(physicsBody != null) {
                if (physicsBody.body.getLinearVelocity().x > 0f) {
                    facing.direction = Facing.Direction.RIGHT;
                } else if (physicsBody.body.getLinearVelocity().x < 0f) {
                    facing.direction = Facing.Direction.LEFT;
                }
            }
        }

        if(facing.direction != facing.defaultDir) {
            sprite.setFlip(true, false);
        }
    }
}
