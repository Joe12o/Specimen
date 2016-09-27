package com.wgdc.platformer.component;

import com.artemis.Component;

public class Lifetime extends Component {
    public long lifetime; // in seconds
    public long birthTime;

    public Lifetime(long lifetime) {
        this.lifetime = lifetime;
        this.birthTime = System.currentTimeMillis();
    }
}
