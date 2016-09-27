package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.wgdc.platformer.component.Facing;

public class DirectedSpriteSystem extends EntityProcessingSystem {

    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<Facing> facingCm;

    public DirectedSpriteSystem() {
        super(Aspect.all(Facing.class, VisSprite.class));
    }

    @Override
    protected void process(Entity e) {
        VisSprite sprite = spriteCm.get(e);
        Facing facing = facingCm.get(e);
        if(facing.direction != facing.defaultDir) {
            sprite.setFlip(true, false);
        }
    }
}
