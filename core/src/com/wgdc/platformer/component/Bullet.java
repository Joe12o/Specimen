package com.wgdc.platformer.component;

import com.artemis.Component;

public class Bullet extends Component {

    public float velX;
    public float velY;

    public Bullet(float velX, float velY) {
        this.velX = velX;
        this.velY = velY;
    }
}
