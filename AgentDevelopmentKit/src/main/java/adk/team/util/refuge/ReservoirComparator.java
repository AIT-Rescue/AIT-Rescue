package adk.team.util.refuge;

import adk.team.util.graph.PositionUtil;
import rescuecore2.misc.Pair;

import java.util.Comparator;

public class ReservoirComparator implements Comparator<Reservoir>{

    private Pair<Integer, Integer> position;

    public ReservoirComparator(Pair<Integer, Integer> selfPosition) {
        this.position = selfPosition;
    }

    @Override
    public int compare(Reservoir o1, Reservoir o2) {
        return (int)(PositionUtil.valueOfCompare(this.position, o1.getLocation()) - PositionUtil.valueOfCompare(this.position, o2.getLocation()));
    }
}
