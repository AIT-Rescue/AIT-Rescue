package adk.team.util.graph;

import com.google.common.base.MoreObjects;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;

public class RouteNode {

    // Node ID (Road or Building)
    private EntityID nodeID;
    // Node Position
    private Pair<Integer, Integer> position;
    // Node is Road
    private Boolean isRoad;

    private boolean passable;

    private Set<EntityID> neighbours;

    public RouteNode(RouteNode original) {
        this.nodeID = original.getID();
        this.position = original.getPosition();
        this.isRoad = original.isRoad();
        this.passable = original.passable();
        this.neighbours = new HashSet<>(original.getNeighbours());
    }

    private RouteNode(StandardWorldModel world, Road road) {
        this.nodeID = road.getID();
        this.position = road.getLocation(world);
        this.isRoad = Boolean.TRUE;
        this.passable = true;
        this.neighbours = new HashSet<>();
    }

    private RouteNode(StandardWorldModel world, Building building) {
        this.nodeID = building.getID();
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
        return this.position.first();
    }

    public int getY() {
        return this.position.second();
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

    public boolean isNeighbourNode(RouteNode node) {
        return this.isNeighbourNode(node.getID());
    }

    public boolean isNeighbourNode(EntityID nodeID) {
        return this.neighbours.contains(nodeID);
    }

    public Set<EntityID> getNeighbours() {
        return this.neighbours;
    }

    public void addNeighbour(RouteNode node) {
        this.addNeighbour(node.getID());
    }

    public void addNeighbour(EntityID id) {
        this.neighbours.add(id);
    }

    public void removeNeighbour(RouteNode node) {
        this.removeNeighbour(node.getID());
    }

    public void removeNeighbour(EntityID id) {
        this.neighbours.remove(id);
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RouteNode && this.equals((RouteNode)o);
    }

    public boolean equals(RouteNode node) {
        return this.nodeID.getValue() == node.getID().getValue();
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("nodeID", this.nodeID)
                .add("posX", this.position.first())
                .add("posY", this.position.second())
                .add("isRoad", this.isRoad)
                .add("passable", this.passable)
                .add("neighbours", this.neighbours)
                .toString();
    }
}
