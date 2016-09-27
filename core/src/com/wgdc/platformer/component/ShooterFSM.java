package com.wgdc.platformer.component;

import com.artemis.Component;
import com.wgdc.platformer.ai.fsm.ShooterAgent;

public class ShooterFSM extends Component {
    public ShooterAgent agent;

    public ShooterFSM(ShooterAgent agent) {
        this.agent = agent;
    }
}
