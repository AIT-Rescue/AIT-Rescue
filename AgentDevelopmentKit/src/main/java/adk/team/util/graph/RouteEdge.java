package adk.team.util.graph;

import com.google.common.collect.Table;
import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;

public class RouteEdge {

    /*public List<EntityID> getRoads();

    public double getDistance();

    public double getDistance(EntityID roadID);
    */
    private Map<EntityID, Table<EntityID, EntityID, Double>> distanceMap;

    private double edgeDistance;

    public boolean addPath(List<EntityID> path) {
        return true;
    }


}
