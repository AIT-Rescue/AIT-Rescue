package adk.team.util.graph;

import adk.team.util.provider.WorldProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteGraph {
    //roadID <neiA, neiB, distance>
    //private Map<EntityID, Table<EntityID, EntityID, Double>> distanceMap;
    private Map<EntityID, RouteNode> nodeMap;
    private Map<EntityID, RouteEdge> edgeMap;
    private Table<EntityID, EntityID, RouteEdge> edgeTable;

    private WorldProvider provider;

    public RouteGraph(WorldProvider worldProvider) {
        //this.distanceMap = new HashMap<>();
        this.provider = worldProvider;
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.edgeTable = HashBasedTable.create();
    }

    public Map<EntityID, Double> getDistanceMap(StandardWorldModel world, List<EntityID> path) {
        Map<EntityID, Double> result = new HashMap<>();
        int size = path.size() - 2;
        for(int i = 1; i < size; i++) {
            EntityID areaID = path.get(i);
            Area area = (Area)world.getEntity(areaID);
            double distance = PositionUtil.pointDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
            result.put(areaID, distance);
        }
        return result;
    }

    public void analysis(StandardWorldModel world) {
        Set<EntityID> processed = new HashSet<>();
        //Buildings
        for(StandardEntity entity : world.getEntitiesOfType(StandardEntityURN.BUILDING, StandardEntityURN.REFUGE)) {
            Building building = (Building)entity;
            EntityID buildingID = building.getID();
            if(!processed.contains(buildingID)) {
                RouteNode node = new RouteNode(buildingID);
                this.nodeMap.put(buildingID, node);
            }
        }
    }
}
