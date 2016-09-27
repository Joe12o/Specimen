package com.wgdc.platformer.ai.fsm;

import com.artemis.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.wgdc.platformer.ai.AStarPathfinder;
import com.wgdc.platformer.ai.PathNode;

public class ShooterAgent {

    public StateMachine<ShooterAgent, ShooterState> stateMachine;
    public Entity shooterEntity;
    public Entity playerEntity;
    public int ticks = 0;
    public PathNode nextNode;
    public AStarPathfinder pathfinder;
    public Body shooterBody;

    public ShooterAgent(Entity shooterEntity, Entity playerEntity, AStarPathfinder pathfinder) {
        this.shooterEntity = shooterEntity;
        this.playerEntity = playerEntity;
        this.stateMachine = new DefaultStateMachine<ShooterAgent, ShooterState>(this);
        this.pathfinder = pathfinder;

        this.shooterBody = shooterEntity.getComponent(PhysicsBody.class).body;

        stateMachine.changeState(ShooterState.IDLE);
    }

    public void update() {
        ticks++;
        stateMachine.update();
    }

    public Vector2 getPosition() {
        return shooterBody.getPosition();
    }

    public Vector2 getPlayerPosition() {
        return playerEntity.getComponent(PhysicsBody.class).body.getPosition();
    }
}
