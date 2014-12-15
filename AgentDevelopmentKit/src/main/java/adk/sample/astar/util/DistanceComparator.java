package adk.sample.astar.util;

import adk.team.util.graph.PositionUtil;
import adk.team.util.graph.RouteGraph;
import adk.team.util.graph.RouteNode;

import java.util.Comparator;

public class DistanceComparator implements Comparator<RouteNode> {

    private RouteGraph graph;

    private RouteNode position;

    private RouteNode goal;

    public DistanceComparator(RouteGraph routeGraph, RouteNode currentNode, RouteNode target) {
        this.graph = routeGraph;
        this.position = currentNode;

    }

    @Override
    public int compare(RouteNode o1, RouteNode o2) {
        double value1 = this.graph.getEdge(this.position, o1).getDistance() + PositionUtil.getDistance(this.goal.getPosition(), o1.getPosition());
        double value2 = this.graph.getEdge(this.position, o2).getDistance() + PositionUtil.getDistance(this.position.getPosition(), o2.getPosition());
        return (int)(value1 - value2);
    }
}
