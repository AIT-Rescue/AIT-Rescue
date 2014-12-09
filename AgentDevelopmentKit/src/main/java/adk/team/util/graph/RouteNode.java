package adk.team.util.graph;

import com.google.common.collect.Lists;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteNode {

    private EntityID nodeID;

    private Set<EntityID> neighbourNode;

    public RouteNode(EntityID areaID) {
        this.nodeID = areaID;
        this.neighbourNode = new HashSet<>();
    }

    public void addNode(EntityID areaID) {
        this.neighbourNode.add(areaID);
    }

    public Set<EntityID> getNeighbourNode() {
        return this.neighbourNode;
    }

    public EntityID getNodeID() {
        return this.nodeID;
    }

    public List<EntityID> getPath() {
        return Lists.newArrayList(this.nodeID);
    }
}
