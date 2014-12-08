package adk.team.util.graph;

import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class RouteNode {

    /*private EntityID nodeID;

    public RouteNode(EntityID id) {
        this.nodeID = id;
    }

    public abstract EntityID[] getNeighbours();

    public abstract RouteNode[] getNodes();

    public abstract RouteEdge[] getEdges();

    public abstract EntityID getNodeID();
    */
    public abstract EntityID getNodeID();

    public abstract List<EntityID> getPath(RouteNode from, EntityID target);

    public RouteNode[] getNeighbourNodes() {
        return null;
    }
}
