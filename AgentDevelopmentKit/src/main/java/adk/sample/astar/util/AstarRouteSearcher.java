package adk.sample.astar.util;

import adk.team.util.RouteSearcher;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class AstarRouteSearcher implements RouteSearcher{
    @Override
    public List<EntityID> noTargetMove(int time) {
        return null;
    }

    @Override
    public List<EntityID> getPath(int time, EntityID from, EntityID to) {
        return null;
    }
}
