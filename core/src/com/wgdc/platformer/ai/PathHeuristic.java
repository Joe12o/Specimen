package com.wgdc.platformer.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class PathHeuristic implements Heuristic<PathNode> {
    @Override
    public float estimate(PathNode node, PathNode endNode) {
        return 0;
    }
}
