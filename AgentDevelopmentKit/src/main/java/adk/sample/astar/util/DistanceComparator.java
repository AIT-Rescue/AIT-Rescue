package adk.sample.astar.util;

import adk.team.util.graph.PositionUtil;
import adk.team.util.graph.RouteNode;

import java.util.Comparator;

public class DistanceComparator implements Comparator<RouteNode> {

    private RouteNode position;

    public DistanceComparator(RouteNode node) {
        this.position = node;
    }

    @Override
    public int compare(RouteNode o1, RouteNode o2) {
        //return o1.length() - o2.length();
        //return 0;
        long result = PositionUtil.valueOfCompare(this.position.getPosition(), o1.getPosition()) - PositionUtil.valueOfCompare(this.position.getPosition(), o2.getPosition());
        return result > 0L ? 1 : result == 0L ? 0 : -1;
    }
}
