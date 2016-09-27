package com.wgdc.platformer.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.wgdc.platformer.ai.AStarPathfinder;
import com.wgdc.platformer.ai.PathMap;
import com.wgdc.platformer.ai.fsm.ShooterAgent;
import com.wgdc.platformer.component.ShooterAi;
import com.wgdc.platformer.component.ShooterFSM;

public class ShooterAiSystem extends EntityProcessingSystem implements AfterSceneInit {

    private ComponentMapper<ShooterFSM> shooterFSMCm;
    private CameraManager camManager;

    private VisIDManager idManager;
    private AStarPathfinder pathFinder;
    public Entity player;
    private boolean ground = false;

    public ShooterAiSystem() {
        super(Aspect.all(ShooterAi.class));
    }

    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        World physWorld = world.getSystem(PhysicsSystem.class).getPhysicsWorld();

        PathMap pathMap = new PathMap(40, 20, 128, 72, camManager.getCamera());
        QueryCallback callback = new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                Entity e = (Entity) fixture.getBody().getUserData();
                VisID id = e.getComponent(VisID.class);
                if(id != null && id.id.equals("ground")) {
                    ground = true;
                }
                return false;
            }
        };

        for(int y = 0; y < pathMap.getHeight(); y++) {
            for(int x = 0; x < pathMap.getWidth(); x++) {
                ground = false;
                physWorld.QueryAABB(callback, x * pathMap.getTileWidth(), y * pathMap.getTileHeight(), (x + 1) * pathMap.getTileWidth(), (y + 1) * pathMap.getTileHeight());
                if(y == 0) {
                    ground = true;
                }
                if(ground) {
                    pathMap.getNodeAt(x, y).ground = true;
                }
            }
        }

        pathFinder = new AStarPathfinder(pathMap);
    }

    public void inserted(Entity e) {
        e.edit().add(new ShooterFSM(new ShooterAgent(e, player, pathFinder)));
    }

    @Override
    protected void process(Entity e) {
        ShooterFSM shooter = shooterFSMCm.get(e);
        shooter.agent.update();
        pathFinder.map.render();
    }
}
