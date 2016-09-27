package com.wgdc.platformer.component;

import com.artemis.Component;

public class Bullet extends Component {

    public float velX;
    public float velY;
    public int damage;
    public boolean playerShot;

    public Bullet(float velX, float velY, int damage, boolean playerShot) {
        this.velX = velX;
        this.velY = velY;
        this.damage = damage;
        this.playerShot = playerShot;
    }
}
