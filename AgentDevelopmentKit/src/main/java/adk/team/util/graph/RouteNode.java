package adk.team.util.graph;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RouteNode {
    // Node ID
    private EntityID nodeID;
    // Node Position
    private int posX;
    private int posY;
    // Node is Road (is not Building)
    private boolean isRoad;
    // Node passable
    private boolean passable;

    //neighbour nodeID <nodeID>
    private Set<EntityID> neighbours;
    //neighbour nodeID <neighbourAreaID nodeID>
    private Map<EntityID, EntityID> neighbourMap; //???

    public boolean isNeighbourNode(RouteNode node) {
        return this.isNeighbourNode(node.getID());
    }

    //Nodeの場合は，Edgeを介して隣接しているNodeのID
    //Edgeの場合は，EdgeにつながっているNodeのID
    public boolean isNeighbourNode(EntityID nodeID) {
        return this.neighbours.contains(nodeID);
    }

    public boolean isNeighbourEdge(RouteEdge edge) {
        return edge.contains(this.nodeID);
    }

    //Nodeの場合は，Edgeを介して隣接しているNodeのIDのSet
    //Edgeの場合は，EdgeにつながっているNodeのIDのSet
    public Set<EntityID> getNeighbours() {
        return this.neighbours;
    }

    public boolean passable() {
        return this.passable;
    }

    public EntityID getID() {
        return this.nodeID;
    }

    public List<EntityID> getPath() {
        return Lists.newArrayList(this.nodeID);
    }

    public int getX() {
        return this.posX;
    }

    public int getY() {
        return this.posY;
    }

    public boolean isRoad() {
        return this.isRoad;
    }

    @Override
    public int hashCode() {
        return this.getID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RouteNode && this.nodeID.getValue() == ((RouteNode)o).getID().getValue();
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("nodeID", this.nodeID)
                .add("posX", this.posX)
                .add("posY", this.posY)
                .add("isRoad", this.isRoad)
                .toString();
    }
}
