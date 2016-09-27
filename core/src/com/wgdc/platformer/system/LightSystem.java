package com.wgdc.platformer.system;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.wgdc.platformer.component.Light;

public class LightSystem extends EntityProcessingSystem {

    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<Light> lightCm;
    private RayHandler handler;

    public LightSystem() {
        super(Aspect.all(Light.class, Transform.class));
    }

    @Override
    protected void process(Entity e) {
        Light light = lightCm.get(e);
        Transform transform = transformCm.get(e);
        if(light.light.getBody() == null) {
            light.light.setPosition(transform.getX() + light.offsetX, transform.getY() + light.offsetY);
        }
    }

    @Override
    public void inserted(Entity e) {
        Light light = lightCm.get(e);
        Transform transform = transformCm.get(e);
        light.light = new PointLight(handler, light.rays, light.color, light.size, transform.getX() + light.offsetX, transform.getY() + light.offsetY);
        PhysicsBody body = e.getComponent(PhysicsBody.class);
        if(body != null) {
            light.light.attachToBody(body.body, light.offsetX, light.offsetY);
            light.light.setIgnoreAttachedBody(true);
        }
    }

    @Override
    public void removed(Entity e) {
        Light light = lightCm.get(e);
        light.light.remove();
    }

    public void setRayHandler(RayHandler handler) {
        this.handler = handler;
    }
}
