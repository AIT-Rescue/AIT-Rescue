package sample.util;

import adk.team.util.graph.PositionUtil;
import adk.team.util.graph.RouteNode;
import rescuecore2.worldmodel.EntityID;

import java.util.Comparator;
import java.util.Map;

public class SampleComparator implements Comparator<RouteNode> {

    private RouteNode goal;

    private Map<EntityID, Double> fromStart;

    public SampleComparator(RouteNode target, Map<EntityID, Double> distanceFromStart) {
        this.goal = target;
        this.fromStart = distanceFromStart;
    }

    @Override
    public int compare(RouteNode node1, RouteNode node2) {
        EntityID id1 = node1.getID();
        EntityID id2 = node2.getID();
        double end1 = PositionUtil.getLinearDistance(this.goal, node1);
        double end2 = PositionUtil.getLinearDistance(this.goal, node2);
        double value1 = fromStart.get(id1) + end1;
        double value2 = fromStart.get(id2) + end2;
        return (int)(value1 - value2);
    }
}
