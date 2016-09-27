package com.wgdc.platformer.component;

import com.artemis.Component;

public class Facing extends Component {

    public enum Direction { LEFT, RIGHT }

    public Direction direction;
    public final Direction defaultDir;

    public Facing(Direction direction) {
        this.direction = direction;
        this.defaultDir = direction;
    }

}
