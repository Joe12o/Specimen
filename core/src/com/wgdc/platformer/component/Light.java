package com.wgdc.platformer.component;

import box2dLight.PositionalLight;
import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class Light extends Component {
    public int rays;
    public Color color;
    public float size;
    public float offsetX;
    public float offsetY;
    public PositionalLight light;

    public Light(int rays, Color color, float size, float offsetX, float offsetY) {
        this.rays = rays;
        this.color = color;
        this.size = size;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
