package adk.team.util.graph;

import com.google.common.collect.Lists;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteNode {
    //nodeID (area.getID())
    private EntityID nodeID;
    //nodeX (area.getX())
    private int x;
    //nodeY (area.getY())
    private int y;
    //neighbour nodeID <nodeID>
    private Set<EntityID> neighbours;
    //neighbour nodeID <neighbourAreaID nodeID>
    private Map<EntityID, EntityID> neighbourMap;

    private boolean passable;

    public RouteNode(StandardWorldModel world, EntityID areaID) {
        this.nodeID = areaID;
        this.neighbours = new HashSet<>();
        this.neighbourMap = new HashMap<>();
        this.passable = true;
        Area area = (Area)world.getEntity(areaID);
        this.x = area.getX();
        this.y = area.getY();
    }

    public void addNode(EntityID neighbourAreaID, EntityID neighbourNodeID) {
        this.neighbours.add(neighbourNodeID);
        this.neighbourMap.put(neighbourAreaID, neighbourNodeID);
    }

    public boolean containsKey(EntityID neighbourAreaID) {
        return this.neighbourMap.containsKey(neighbourAreaID);
    }

    public boolean contains(EntityID neighbourNodeID) {
        return this.neighbours.contains(neighbourNodeID);
    }

    public Set<EntityID> getNeighbours() {
        return this.neighbours;
    }

    public EntityID getNeighbourNode(EntityID neighborID) {
        return this.neighbourMap.get(neighborID);
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

    public boolean passable() {
        return this.passable;
    }

    public void setPassable(boolean flag) {
        this.passable = flag;
    }
}
