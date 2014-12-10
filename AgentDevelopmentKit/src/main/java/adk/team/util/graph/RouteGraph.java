package adk.team.util.graph;

import adk.team.util.provider.WorldProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
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
        this.analysis(this.provider.getWorld());
    }

    public void analysis(StandardWorldModel world) {
        //edge road only
        Set<StandardEntity> processedRoad = new HashSet<>();
        //Buildings
        for(StandardEntity entity : world.getEntitiesOfType(StandardEntityURN.BUILDING, StandardEntityURN.REFUGE)) {
            Building building = (Building)entity;
            EntityID buildingID = building.getID();
            RouteNode routeNode = this.nodeMap.get(buildingID);
            RouteNode buildingNode = routeNode != null ? routeNode : new RouteNode(buildingID);
            for(EntityID id : building.getNeighbours()) {
                if(buildingNode.contains(id)) {
                    continue;
                }
                List<EntityID> path = Lists.newArrayList(buildingID);
                Area area = (Area)world.getEntity(id);
                EntityID neighbourID = buildingID;
                boolean edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbours = area.getNeighbours();
                    if(area instanceof Building) {
                        edgeFlag = false;
                        path.add(areaID);
                        RouteNode node = new RouteNode(areaID);
                        node.addNode(id, buildingID);
                        buildingNode.addNode(areaID, id);
                        this.nodeMap.put(areaID, node);
                    }
                    if(neighbours.size() == 2) {
                        neighbours.remove(neighbourID);
                        path.add(areaID);
                        processedRoad.add((StandardEntity)area);
                        EntityID nextID = neighbours.get(0);
                        area = (Area)world.getEntity(nextID);
                        neighbourID = areaID;
                    }
                }
                RouteEdge edge = new RouteEdge(world, path);
                int size = path.size() - 1;
                for(int i = 1; i < size; i++) {
                    this.edgeMap.put(path.get(i), edge);
                }
            }
            this.nodeMap.put(buildingID, buildingNode);
        }
    }
}
