package adk.sample.astar.util;

import adk.team.util.graph.PositionUtil;
import adk.team.util.graph.RouteNode;
import rescuecore2.worldmodel.EntityID;

import java.util.Comparator;
import java.util.Map;

public class DistanceComparator implements Comparator<RouteNode> {

    private RouteNode goal;

    private Map<EntityID, Double> fromStart;

    private Map<EntityID, Double> toEnd;

    public DistanceComparator(RouteNode target, Map<EntityID, Double> distanceFromStart, Map<EntityID, Double> distanceToEnd) {
        this.goal = target;
        this.fromStart = distanceFromStart;
        this.toEnd = distanceToEnd;
    }

    @Override
    public int compare(RouteNode node1, RouteNode node2) {
        EntityID id1 = node1.getID();
        EntityID id2 = node2.getID();
        double end1;
        double end2;
        if(this.toEnd.containsKey(id1)) {
            end1 = this.toEnd.get(id1);
        }
        else {
            end1 = PositionUtil.getDistance(this.goal.getPosition(), node1.getPosition());
            this.toEnd.put(id1, end1);
        }
        if(this.toEnd.containsKey(id2)) {
            end2 = this.toEnd.get(id2);
        }
        else {
            end2 = PositionUtil.getDistance(this.goal.getPosition(), node2.getPosition());
            this.toEnd.put(id2, end1);
        }
        double value1 = fromStart.get(id1) + end1;
        double value2 = fromStart.get(id2) + end2;
        return (int)(value1 - value2);
    }
}
