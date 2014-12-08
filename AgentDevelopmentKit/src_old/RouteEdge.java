package adk.team.util;

import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;

public class RouteEdge {

    private List<EntityID> edges;
    private long edgeDistance;
    private Map<EntityID, Long> roadDistance;
    private RouteNode startNode;
    private RouteNode endNode;
    private Map<EntityID, List<EntityID>> buildingPath;
    private Map<EntityID, List<EntityID>> nearPath;
    private List<EntityID> unPassableRoad;

    public RouteNode nearNode(EntityID id) {
        if(this.buildingPath.containsKey(id)) {
            List<EntityID> path = this.buildingPath.get(id);
        }
        return null;
    }

}
