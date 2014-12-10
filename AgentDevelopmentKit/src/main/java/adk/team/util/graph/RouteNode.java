package adk.team.util.graph;

import com.google.common.collect.Lists;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteNode {

    private EntityID nodeID;
    //nodeID
    private Set<EntityID> neighbourNode;
    //neighbourID nodeID
    private Map<EntityID, EntityID> nodeMap;

    public RouteNode(EntityID areaID) {
        this.nodeID = areaID;
        this.neighbourNode = new HashSet<>();
        this.nodeMap = new HashMap<>();
    }

    public void addNode(EntityID areaID) {
        this.neighbourNode.add(areaID);
    }

    public void removeNode(EntityID areaID) {
        this.neighbourNode.remove(areaID);
    }

    public void addNode(EntityID neighbourID, EntityID areaID) {
        this.neighbourNode.add(areaID);
        this.nodeMap.put(neighbourID, areaID);
    }

    public boolean contains(EntityID neighbourID) {
        return this.nodeMap.containsKey(neighbourID);
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
