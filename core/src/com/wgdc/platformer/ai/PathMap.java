package com.wgdc.platformer.ai;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class PathMap {

    private ShapeRenderer renderer;
    private OrthographicCamera camera;

    private PathNode[][] map;
    private final int width;
    private final int height;
    private final float tileWidth;
    private final float tileHeight;

    public PathMap(int width, int height, float levelWidth, float levelHeight, OrthographicCamera camera) {
        this.width = width;
        this.height = height;
        this.tileWidth = levelWidth / width;
        this.tileHeight = levelHeight / height;
        this.renderer = new ShapeRenderer();
        this.camera = camera;

        map = new PathNode[height][width];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                map[y][x] = new PathNode(this, x, y);
            }
        }
    }

    public void render() {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                PathNode node = map[y][x];
                if(node.ground) {
                    renderer.setColor(0, 1f, 0, 1f);
                } else {
                    renderer.setColor(1f, 0, 0, 1f);
                }
                renderer.rect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

                renderer.setColor(Color.YELLOW);
                for(Connection<PathNode> con : node.getConnections()) {
                    PathNode other = con.getToNode();
                    renderer.line(x * tileWidth + (tileWidth / 2), y * tileHeight + (tileHeight / 2), other.x * tileWidth + (tileWidth / 2), other.y * tileHeight + (tileHeight / 2));
                }
            }
        }
        renderer.end();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public float getTileHeight() {
        return tileHeight;
    }

    public PathNode getNodeAt(int x, int y) {
        return map[y][x];
    }

    public PathNode getNodeAtWorldCoords(float worldX, float worldY) {
        int x = MathUtils.floor(worldX / tileWidth);
        int y = MathUtils.floor(worldY / tileHeight);
        if(x >= 0 && x < width && y >= 0 && y < height) {
            return map[y][x];
        } else {
            return null;
        }
    }
}
