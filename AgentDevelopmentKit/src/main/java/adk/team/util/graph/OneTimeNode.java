package adk.team.util.graph;

import com.google.common.collect.Lists;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class OneTimeNode {
    private EntityID nodeID;
    private EntityID firstNeighbour;
    private EntityID secondNeighbour;
    private List<EntityID> firstNeighbourPath;
    private List<EntityID> secondNeighbourPath;

    public static OneTimeNode create(EntityID areaID, RouteEdge edge) {
        if(edge.contains(areaID)) {
            return new OneTimeNode(areaID, edge);
        }
        return null;
    }

    private OneTimeNode(EntityID areaID, RouteEdge edge) {
        this.nodeID = areaID;
        this.firstNeighbour = edge.getFirstNodeID();
        this.secondNeighbour = edge.getSecondNodeID();
        this.firstNeighbourPath = Lists.newArrayList(this.firstNeighbour);
        this.firstNeighbourPath.addAll(edge.getPath(this.firstNeighbour, areaID));
        this.secondNeighbourPath = edge.getPath(areaID, this.secondNeighbour);
        this.secondNeighbourPath.add(this.secondNeighbour);
    }

    public EntityID getNodeID() {
        return this.nodeID;
    }

    public EntityID getFirstNeighbour() {
        return this.firstNeighbour;
    }

    public EntityID getSecondNeighbour() {
        return this.secondNeighbour;
    }

    public List<EntityID> getFirstNeighbourPath() {
        return this.firstNeighbourPath;
    }

    public List<EntityID> getSecondNeighbourPath() {
        return this.secondNeighbourPath;
    }
}
