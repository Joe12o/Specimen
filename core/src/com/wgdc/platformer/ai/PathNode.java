package com.wgdc.platformer.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class PathNode {

    public final int x;
    public final int y;
    public final float worldX;
    public final float worldY;
    public boolean ground;
    private final int index;
    private final Array<Connection<PathNode>> connections;

    public PathNode(PathMap map, int x, int y) {
        this.x = x;
        this.y = y;
        this.worldX = x * map.getTileWidth();
        this.worldY = y * map.getTileHeight();
        this.index = x * map.getHeight() + y;
        this.ground = false;
        this.connections = new Array<Connection<PathNode>>();
    }

    public int getIndex () {
        return index;
    }

    public Array<Connection<PathNode>> getConnections () {
        return connections;
    }
}
