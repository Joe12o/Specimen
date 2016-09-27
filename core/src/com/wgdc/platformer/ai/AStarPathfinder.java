package com.wgdc.platformer.ai;

import com.badlogic.gdx.ai.pfa.*;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AStarPathfinder {

    public final PathMap map;
    private final PathFinder<PathNode> pathfinder;
    private final Heuristic<PathNode> heuristic;
    private final GraphPath<Connection<PathNode>> connectionPath;

    public AStarPathfinder(PathMap map) {
        this.map = map;
        this.pathfinder = new IndexedAStarPathFinder<PathNode>(createGraph(map));
        this.connectionPath = new DefaultGraphPath<Connection<PathNode>>();
        this.heuristic = new PathHeuristic();
    }

    public PathNode findNextNode(Vector2 source, Vector2 target) {
        PathNode sourceNode = map.getNodeAtWorldCoords(source.x, source.y);
        PathNode targetNode = map.getNodeAtWorldCoords(target.x, target.y);

        if(sourceNode != null && targetNode != null) {
            connectionPath.clear();
            pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, connectionPath);
        } else {
            return null;
        }

        return connectionPath.getCount() == 0 ? null : connectionPath.get(0).getToNode();
    }

    public static PathGraph createGraph(PathMap map) {
        final int height = map.getHeight();
        final int width = map.getWidth();
        PathGraph graph = new PathGraph(map);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                PathNode node = map.getNodeAt(x, y);
                if (!node.ground) {
                    continue;
                }

                // Add a connection for each valid neighbor
                PathNode other, left, right;

                // Search for ground next to this node
                if(node.x - 1 >= 0) {
                    other = map.getNodeAt(node.x - 1, node.y);
                    if(other.ground) {
                        node.getConnections().add(new DefaultConnection<PathNode>(node, other));
                    }
                }

                // Search for ground next to this node
                if(node.x + 1 < map.getWidth()) {
                    other = map.getNodeAt(node.x + 1, node.y);
                    if(other.ground) {
                        node.getConnections().add(new DefaultConnection<PathNode>(node, other));
                    }
                }

                // Search for walkable area up to 6 nodes above
                for(int i = 0; i < 6 && node.y + i < map.getHeight(); i++) {
                    other = map.getNodeAt(node.x, node.y + i);
                    left = node.x > 0 ? map.getNodeAt(node.x - 1, node.y + i) : null;
                    right = node.x < map.getWidth() - 1 ? map.getNodeAt(node.x + 1, node.y + i) : null;
                    if(other.ground && ((left != null && !left.ground) || ((right != null && !right.ground)))) {
                        node.getConnections().add(new DefaultConnection<PathNode>(node, other));
                    }
                }

                // Search for walkable area below
                for(int j = node.y; j >= 0; j--) {
                    other = map.getNodeAt(node.x, node.y - j);
                    if(other.ground) {
                        //node.getConnections().add(new DefaultConnection<PathNode>(node, other));
                        break;
                    }
                }
            }
        }
        return graph;
    }

    private static class PathGraph implements IndexedGraph<PathNode> {

        private PathMap map;

        public PathGraph(PathMap map) {
            this.map = map;
        }

        @Override
        public int getIndex(PathNode node) {
            return node.getIndex();
        }

        @Override
        public int getNodeCount() {
            return map.getHeight() * map.getWidth();
        }

        @Override
        public Array<Connection<PathNode>> getConnections(PathNode fromNode) {
            return fromNode.getConnections();
        }
    }

}
