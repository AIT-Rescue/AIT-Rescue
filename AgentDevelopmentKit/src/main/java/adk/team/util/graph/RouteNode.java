package adk.team.util.graph;

import adk.team.util.provider.WorldProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Edge;
import rescuecore2.standard.entities.Road;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RouteNode implements GraphElement {

    private EntityID nodeID;
    private Table<EntityID, EntityID, Double> distanceTable;
    //private Map<EntityID, RouteNode> neighbourNode;
    //private Map<EntityID, Edge> neighbourEdge;
    private Set<EntityID> neighbourNodes;

    private RouteGraph graph;
    private WorldProvider provider;


    public RouteNode(Area area, RouteGraph routeGraph, WorldProvider worldProvider) {
        this.nodeID = area.getID();
        this.graph = routeGraph;
        this.provider = worldProvider;
        this.distanceTable = HashBasedTable.create();
        this.neighbourNodes = new HashSet<>();

    }

    public void addNeighbourNode(EntityID id) {
        this.neighbourNodes.add(id);
    }

    public Set<EntityID> getNeighbourNodes() {
        return this.neighbourNodes;
    }

    /*public RouteNode(EntityID areaID, RouteGraph routeGraph, WorldProvider worldProvider) {
        this.nodeID = areaID;
        this.graph = routeGraph;
        this.provider = worldProvider;
        this.distanceTable = HashBasedTable.create();
        Area area = (Area)worldProvider.getWorld().getEntity(areaID);
    }*/

    public EntityID getNodeID() {
        return this.nodeID;
    }

    @Override
    public boolean addPath(List<EntityID> path) {
        path.add(getNodeID());
        return true;
    }

    public double getDistance(EntityID from, EntityID to) {
        Double distance = this.distanceTable.get(from, to);
        if(distance != null) {
            return distance;
        }
        Area area = (Area)this.provider.getWorld().getEntity(this.getNodeID());
        List<EntityID> neighbours = area.getNeighbours();
        if(neighbours.contains(from) && neighbours.contains(to)) {
            double result = PositionUtil.pointDistance(area.getEdgeTo(from), area.getEdgeTo(to));
            this.distanceTable.put(from, to, result);
            this.distanceTable.put(to, from, result);
            return result;
        }
        return Double.NaN;
    }
}
