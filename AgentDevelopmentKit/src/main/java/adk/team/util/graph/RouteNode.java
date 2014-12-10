package adk.team.util.graph;

import com.google.common.collect.Lists;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteNode {

    private EntityID nodeID;
    private int x;
    private int y;
    //nodeID
    private Set<EntityID> neighbourNode;
    //neighbourID nodeID
    private Map<EntityID, EntityID> nodeMap;

    public RouteNode(StandardWorldModel world, EntityID areaID) {
        this.nodeID = areaID;
        this.neighbourNode = new HashSet<>();
        this.nodeMap = new HashMap<>();
        Area area = (Area)world.getEntity(areaID);
        this.x = area.getX();
        this.y = area.getY();
    }

    /*
    public void addNode(EntityID areaID) {
        this.neighbourNode.add(areaID);
    }
    */

    public void addNode(EntityID neighbourID, EntityID areaID) {
        this.neighbourNode.add(areaID);
        this.nodeMap.put(neighbourID, areaID);
    }

    public boolean contains(EntityID neighbourID) {
        return this.nodeMap.containsKey(neighbourID);
    }

    public Set<EntityID> getNeighbourNodes() {
        return this.neighbourNode;
    }

    public EntityID getNeighbourNode(EntityID neighborID) {
        return this.nodeMap.get(neighborID);
    }

    public EntityID getNodeID() {
        return this.nodeID;
    }

    public List<EntityID> getPath() {
        return Lists.newArrayList(this.nodeID);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return  this.y;
    }
}
