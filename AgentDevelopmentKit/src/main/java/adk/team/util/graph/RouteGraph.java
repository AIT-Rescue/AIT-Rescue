package adk.team.util.graph;

import adk.team.util.provider.WorldProvider;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteGraph {
    private Map<EntityID, RouteNode> nodeMap;
    private Map<EntityID, RouteEdge> edgeMap;
    private Table<EntityID, EntityID, RouteEdge> edgeTable;

    public RouteGraph(WorldProvider worldProvider) {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.edgeTable = HashBasedTable.create();
        this.analysis(worldProvider.getWorld());
    }

    private void analysis(StandardWorldModel world) {
        Set<StandardEntity> processedRoad = this.analysisBuilding(world);
        Collection<StandardEntity> roads = world.getEntitiesOfType(StandardEntityURN.ROAD, StandardEntityURN.HYDRANT);
        roads.removeAll(processedRoad);
        this.analysisRoad(world, roads);
    }

    private void analysisRoad(StandardWorldModel world, Collection<StandardEntity> roads) {
        Set<StandardEntity> processedRoad = new HashSet<>();
        for(StandardEntity entity : roads) {
            if(processedRoad.contains(entity)) {
                continue;
            }
            Road road = (Road)entity;
            EntityID roadID = road.getID();
            List<EntityID> roadNeighbours = road.getNeighbours();
            if(roadNeighbours.isEmpty()) {
                this.nodeMap.put(roadID, new RouteNode(roadID));
            }
            else if(roadNeighbours.size() == 1) {
                if(this.nodeMap.containsKey(roadID)) {
                    continue;
                }
                RouteNode roadNode = new RouteNode(roadID);
                List<EntityID> path = Lists.newArrayList(roadID);
                EntityID roadNeighbourID = roadNeighbours.get(0);
                Area area = (Area)world.getEntity(roadNeighbourID);
                EntityID neighbourID = roadID;
                boolean edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbourList = area.getNeighbours();
                    path.add(areaID);
                    if(neighbourList.size() == 2) {
                        neighbourList.remove(neighbourID);
                        processedRoad.add(area);
                        neighbourID = areaID;
                        area = (Area)world.getEntity(neighbourList.get(0));
                    }
                    else {
                        edgeFlag = false;
                        if (neighbourList.isEmpty()) {
                            System.out.println("[ERROR] Bad Map (unknown Area)");
                        }
                        else {
                            if(neighbourList.size() == 1) {
                                processedRoad.add(area);
                            }
                            RouteNode routeNode = this.nodeMap.get(areaID);
                            RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                            node.addNode(neighbourID, roadID);
                            roadNode.addNode(roadNeighbourID, areaID);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                }
                this.nodeMap.put(roadID, roadNode);
                RouteEdge edge = new RouteEdge(world, path);
                int size = path.size() - 1;
                for(int i = 1; i < size; i++) {
                    this.edgeMap.put(path.get(i), edge);
                }
                this.edgeTable.put(path.get(0), path.get(size), edge);
                this.edgeTable.put(path.get(size), path.get(0), edge);
            }
            else if(roadNeighbours.size() >= 3) {
                RouteNode routeNode = this.nodeMap.get(roadID);
                RouteNode roadNode = routeNode != null ? routeNode : new RouteNode(roadID);
                for(EntityID id : roadNeighbours) {
                    if(roadNode.contains(id)) {
                        continue;
                    }
                    List<EntityID> path = Lists.newArrayList(roadID);
                    Area area = (Area)world.getEntity(id);
                    EntityID neighbourID = roadID;
                    boolean edgeFlag = true;
                    while (edgeFlag) {
                        EntityID areaID = area.getID();
                        List<EntityID> neighbourList = area.getNeighbours();
                        int neighbourListSize = neighbourList.size();
                        if(neighbourListSize == 2) {
                            neighbourList.remove(neighbourID);
                            path.add(areaID);
                            processedRoad.add(area);
                            neighbourID = areaID;
                            area = (Area)world.getEntity(neighbourList.get(0));
                        }
                        else if(neighbourListSize >= 3) {
                            edgeFlag = false;
                            path.add(areaID);
                            routeNode = this.nodeMap.get(areaID);
                            RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                            node.addNode(neighbourID, roadID);
                            roadNode.addNode(id, areaID);
                            this.nodeMap.put(areaID, node);
                        }
                        else if(neighbourListSize == 1) {
                            edgeFlag = false;
                            processedRoad.add(area);
                            path.add(areaID);
                            routeNode = this.nodeMap.get(areaID);
                            RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                            node.addNode(neighbourID, roadID);
                            roadNode.addNode(id, areaID);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                    this.nodeMap.put(roadID, roadNode);
                    RouteEdge edge = new RouteEdge(world, path);
                    int size = path.size() - 1;
                    for(int i = 1; i < size; i++) {
                        this.edgeMap.put(path.get(i), edge);
                    }
                    this.edgeTable.put(path.get(0), path.get(size), edge);
                    this.edgeTable.put(path.get(size), path.get(0), edge);
                }
            }
            else if(roadNeighbours.size() == 2) {
                if (this.edgeMap.containsKey(roadID)) {
                    continue;
                }
                System.out.println("aaa");
            }
        }
    }

    private Set<StandardEntity> analysisBuilding(StandardWorldModel world) {
        //edge road only
        Set<StandardEntity> processedRoad = new HashSet<>();
        //Buildings
        for(StandardEntity entity : world.getEntitiesOfType(StandardEntityURN.BUILDING, StandardEntityURN.REFUGE)) {
            Building building = (Building)entity;
            EntityID buildingID = building.getID();
            RouteNode routeNode = this.nodeMap.get(buildingID);
            RouteNode buildingNode = routeNode != null ? routeNode : new RouteNode(buildingID);
            List<EntityID> buildingNeighbours = building.getNeighbours();
            if(buildingNeighbours.isEmpty()) {
                this.nodeMap.put(buildingID, buildingNode);
                continue;
            }
            for(EntityID id : buildingNeighbours) {
                if(buildingNode.contains(id)) {
                    continue;
                }
                List<EntityID> path = Lists.newArrayList(buildingID);
                Area area = (Area)world.getEntity(id);
                EntityID neighbourID = buildingID;
                boolean edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbourList = area.getNeighbours();
                    int neighbourListSize = neighbourList.size();
                    path.add(areaID);
                    if(area instanceof Building) {
                        edgeFlag = false;
                        routeNode = this.nodeMap.get(areaID);
                        RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                        node.addNode(neighbourID, buildingID);
                        buildingNode.addNode(id, areaID);
                        this.nodeMap.put(areaID, node);
                    }
                    else if(neighbourList.size() == 2) {
                        neighbourList.remove(neighbourID);
                        processedRoad.add(area);
                        neighbourID = areaID;
                        area = (Area)world.getEntity(neighbourList.get(0));
                    }
                    else if(neighbourListSize >= 3) {
                        edgeFlag = false;
                        routeNode = this.nodeMap.get(areaID);
                        RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                        node.addNode(neighbourID, buildingID);
                        buildingNode.addNode(id, areaID);
                        this.nodeMap.put(areaID, node);
                    }
                    else if(neighbourListSize == 1) {
                        edgeFlag = false;
                        processedRoad.add(area);
                        routeNode = this.nodeMap.get(areaID);
                        RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                        node.addNode(neighbourID, buildingID);
                        buildingNode.addNode(id, areaID);
                        this.nodeMap.put(areaID, node);
                    }
                    else {
                        edgeFlag = false;
                        if(neighbourList.isEmpty()) {
                            System.out.println("[ERROR] Bad Map (unknown Area)");
                        }
                        else {
                            if(neighbourList.size() == 1) {
                                processedRoad.add(area);
                            }
                            edgeFlag = false;
                            routeNode = this.nodeMap.get(areaID);
                            RouteNode node = routeNode != null ? routeNode : new RouteNode(areaID);
                            node.addNode(neighbourID, buildingID);
                            buildingNode.addNode(id, areaID);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                }
                RouteEdge edge = new RouteEdge(world, path);
                int size = path.size() - 1;
                for(int i = 1; i < size; i++) {
                    this.edgeMap.put(path.get(i), edge);
                }
                this.edgeTable.put(path.get(0), path.get(size), edge);
                this.edgeTable.put(path.get(size), path.get(0), edge);
            }
            this.nodeMap.put(buildingID, buildingNode);
        }
        return processedRoad;
    }
}
