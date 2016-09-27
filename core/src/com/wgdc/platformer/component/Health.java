package com.wgdc.platformer.component;

import com.artemis.Component;

public class Health extends Component {
    public int max;
    public int current;

    public Health(int max) {
        this.max = max;
        this.current = max;
    }
}
