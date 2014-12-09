package adk.team.util.graph;

import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Edge;
import rescuecore2.standard.entities.Road;
import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;

public abstract class RouteNode {

    private EntityID nodeID;
    //private Table<EntityID, EntityID, Edge> neighbourEdge;
    private Map<EntityID, RouteNode> neighbourNode;
    private Map<EntityID, Edge> neighbourEdge;

    private RouteGraph graph;

    public RouteNode(RouteGraph routeGraph, EntityID areaID) {
        this.nodeID = areaID;
        this.graph = routeGraph;
    }

    public RouteNode(RouteGraph routeGraph, Area area) {
        this(routeGraph, area.getID());
    }

    public EntityID getNodeID() {
        return this.nodeID;
    }

    public abstract List<EntityID> getPath(RouteNode from, EntityID target);

    public boolean addPath(List<EntityID> path) {
        path.add(getNodeID());
        return true;
    }
}
