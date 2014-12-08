package adk.team.util.graph;

import rescuecore2.worldmodel.EntityID;

import java.util.List;

public interface RouteEdge {

    public List<EntityID> getRoads();

    public double getDistance();

    public double getDistance(EntityID roadID);


}
