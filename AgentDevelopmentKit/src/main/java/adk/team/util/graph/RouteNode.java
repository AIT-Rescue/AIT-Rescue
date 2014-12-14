package adk.team.util.graph;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RouteNode {

    // Node ID (Road or Building)
    private EntityID nodeID;
    // Node Position
    private Pair<Integer, Integer> position;
    private int posX;
    private int posY;
    // Node is Road
    private Boolean isRoad;

    private boolean passable;

    private Set<EntityID> neighbours;

    public RouteNode(RouteNode original) {
        this.nodeID = original.getID();
        this.posX = original.getX();
        this.posY = original.getY();
        this.position = original.getPosition();
        this.isRoad = original.isRoad();
        this.passable = original.passable();
        this.neighbours = new HashSet<>(original.getNeighbours());
    }

    private RouteNode(StandardWorldModel world, Road road) {
        this.nodeID = road.getID();
        this.posX = road.getX();
        this.posY = road.getY();
        this.position = road.getLocation(world);
        this.isRoad = Boolean.TRUE;
        this.passable = true;
        this.neighbours = new HashSet<>();
    }

    private RouteNode(StandardWorldModel world, Building building) {
        this.nodeID = building.getID();
        this.posX = building.getX();
        this.posY = building.getY();
        this.position = building.getLocation(world);
        this.isRoad = Boolean.FALSE;
        this.passable = true;
        this.neighbours = new HashSet<>();
    }

    public static RouteNode getInstance(StandardWorldModel world, Road road) {
        return world != null && road != null ? new RouteNode(world, road) : null;
    }

    public static RouteNode getInstance(StandardWorldModel world, Building building) {
        return world != null && building != null ? new RouteNode(world, building) : null;
    }

    public static RouteNode getInstance(StandardWorldModel world, Area area) {
        if(world != null && area != null) {
            if(area instanceof Road) {
                return new RouteNode(world, (Road)area);
            }
            if(area instanceof Building) {
                return new RouteNode(world, (Building)area);
            }
        }
        return null;
    }

    public static RouteNode getInstance(StandardWorldModel world, EntityID areaID) {
        if(world != null && areaID != null) {
            StandardEntity area = world.getEntity(areaID);
            if(area instanceof Road) {
                return new RouteNode(world, (Road)area);
            }
            if(area instanceof Building) {
                return new RouteNode(world, (Building)area);
            }
        }
        return null;
    }

    public EntityID getID() {
        return this.nodeID;
    }

    public int getX() {
        return this.posX;
    }

    public int getY() {
        return this.posY;
    }

    public Pair<Integer, Integer> getPosition() {
        return this.position;
    }

    public boolean isRoad() {
        return this.isRoad;
    }

    public boolean passable() {
        return this.passable;
    }

    public void setPassable(boolean flag) {
        this.passable = flag;
    }

    public boolean isNeighbourNode(RouteNode node) {
        return this.isNeighbourNode(node.getID());
    }

    public boolean isNeighbourNode(EntityID nodeID) {
        return this.neighbours.contains(nodeID);
    }

    public boolean isSingleNode() {
        if(this.neighbours.isEmpty()) {
            return true;
        }
        for(EntityID id : this.neighbours) {
            if(this.nodeID.getValue() != id.getValue()) {
                return false;
            }
        }
        return true;
    }

    public Set<EntityID> getNeighbours() {
        return this.neighbours;
    }

    public void addNode(RouteNode node) {
        this.addNode(node.getID());
    }

    public void addNode(EntityID id) {
        this.neighbours.add(id);
    }

    public void removeNode(RouteNode node) {
        this.removeNode(node.getID());
    }

    public void removeNode(EntityID id) {
        this.neighbours.remove(id);
    }

    public List<EntityID> getPath() {
        return Lists.newArrayList(this.nodeID);
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RouteNode && this.nodeID.getValue() == ((RouteNode)o).getID().getValue();
    }

    public boolean equals(RouteNode node) {
        return this.nodeID.getValue() == node.getID().getValue();
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("nodeID", this.nodeID)
                .add("posX", this.posX)
                .add("posY", this.posY)
                .add("isRoad", this.isRoad)
                .add("passable", this.passable)
                .add("neighbours", this.neighbours)
                .toString();
    }
}
