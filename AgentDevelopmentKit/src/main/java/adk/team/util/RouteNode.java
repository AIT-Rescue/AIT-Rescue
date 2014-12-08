package adk.team.util;

import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;

public class RouteNode {

    private EntityID roadID;
    private List<EntityID> neighbours;
    private Map<EntityID, Map<EntityID, Long>> distanceMap;

    public RouteNode(EntityID id, List<EntityID> list) {
        this.roadID = id;
        this.neighbours = list;
    }
}
