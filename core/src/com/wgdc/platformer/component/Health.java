package com.wgdc.platformer.component;

import com.artemis.Component;

public class Health extends Component {
    public final int max;
    private int current;
    public boolean hurt = false;

    public Health(int max) {
        this.max = max;
        this.current = max;
    }

    public void damage(int amount) {
        hurt = true;
        current -= amount;
    }

    public int getCurrent() {
        return current;
    }
}
