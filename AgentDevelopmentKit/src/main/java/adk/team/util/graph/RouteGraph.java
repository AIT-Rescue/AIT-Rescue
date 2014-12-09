package adk.team.util.graph;

import adk.team.util.provider.WorldProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Edge;
import rescuecore2.worldmodel.EntityID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteGraph {
    //roadID <neiA, neiB, distance>
    private Map<EntityID, Table<EntityID, EntityID, Double>> distanceMap;
    private Map<EntityID, RouteNode> nodeMap;
    private Map<EntityID, RouteEdge> edgeMap;
    private Table<EntityID, EntityID, RouteEdge> edgeTable;

    private WorldProvider provider;

    public RouteGraph(WorldProvider worldProvider) {
        this.distanceMap = new HashMap<>();
        this.provider = worldProvider;
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.edgeTable = HashBasedTable.create();
    }

    public double getDistance(EntityID areaID, EntityID from, EntityID to) {
        Table<EntityID, EntityID, Double> distanceTable;
        if(this.distanceMap.containsKey(areaID)) {
            distanceTable = this.distanceMap.get(areaID);
            if(distanceTable.contains(from, to)) {
                return distanceTable.get(from, to);
            }
        }
        else {
            distanceTable = HashBasedTable.create();
        }
        Area area = (Area)this.provider.getWorld().getEntity(areaID);
        List<EntityID> neighbours = area.getNeighbours();
        if(neighbours.contains(from) && neighbours.contains(to)) {
            double distance = PositionUtil.pointDistance(area.getEdgeTo(from), area.getEdgeTo(to));
            distanceTable.put(from, to, distance);
            distanceTable.put(to, from, distance);
            this.distanceMap.put(areaID, distanceTable);
            return distance;
        }
        return Double.NaN;
    }

    public void setDistanceTable(EntityID areaID){

    }
}
