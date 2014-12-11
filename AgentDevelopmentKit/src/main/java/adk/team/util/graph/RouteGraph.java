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

    private Table<EntityID, EntityID, Double> neighbourDistance;

    public RouteGraph(WorldProvider worldProvider) {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.edgeTable = HashBasedTable.create();
        this.analysis(worldProvider.getWorld());
    }

    public RouteNode getNode(EntityID nodeID) {
        return this.nodeMap.get(nodeID);
    }

    public boolean containsNode(EntityID nodeID) {
        return this.nodeMap.containsKey(nodeID);
    }

    public RouteEdge getEdge(EntityID roadID) {
        return this.edgeMap.get(roadID);
    }

    public boolean containsEdge(EntityID roadID) {
        return this.edgeMap.containsKey(roadID);
    }

    public RouteEdge getEdge(EntityID start, EntityID end) {
        return this.edgeTable.get(start, end);
    }

    public boolean containsEdge(EntityID start, EntityID end) {
        return this.edgeTable.contains(start, end);
    }

    public boolean contains(EntityID areaID) {
        return this.nodeMap.containsKey(areaID) || this.edgeMap.containsKey(areaID);
    }

    private int time = -1;
    private Map<EntityID, RouteEdge> originalEdgeMap;
    private Set<OneTimeNode> oneTimeNodes;

    public OneTimeNode create(int currentTime, StandardWorldModel world, EntityID areaID) {
        if(this.time != currentTime) {
            this.time = currentTime;
            for(EntityID id: this.originalEdgeMap.keySet()) {
                this.edgeMap.put(id, this.originalEdgeMap.get(id));
            }
            this.originalEdgeMap = new HashMap<>();
            for(OneTimeNode node : this.oneTimeNodes) {
                EntityID nodeID = node.getNodeID();
                EntityID firstNeighbour = node.getFirstNeighbour();
                EntityID secondNeighbour = node.getSecondNeighbour();
                this.edgeTable.remove(nodeID, firstNeighbour);
                this.edgeTable.remove(firstNeighbour, nodeID);
                this.edgeTable.remove(nodeID, secondNeighbour);
                this.edgeTable.remove(secondNeighbour, nodeID);
            }
            this.oneTimeNodes = new HashSet<>();
        }
        if(this.nodeMap.containsKey(areaID)) {
            return null;
        }
        RouteEdge routeEdge = this.edgeMap.get(areaID);
        OneTimeNode node = OneTimeNode.create(areaID, routeEdge);
        if(node == null) {
            return null;
        }
        for(EntityID id : routeEdge.getPath(routeEdge.getFirstNodeID())) {
            this.originalEdgeMap.put(id, this.edgeMap.get(id));
        }
        this.register(world, node.getFirstNeighbourPath());
        this.register(world, node.getSecondNeighbourPath());
        this.oneTimeNodes.add(node);
        return node;
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
                this.nodeMap.put(roadID, new RouteNode(world, roadID));
            }
            else if(roadNeighbours.size() == 1) {
                if(this.nodeMap.containsKey(roadID)) {
                    continue;
                }
                RouteNode roadNode = new RouteNode(world, roadID);
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
                            RouteNode node = routeNode != null ? routeNode : new RouteNode(world, areaID);
                            node.addNode(neighbourID, roadID);
                            roadNode.addNode(roadNeighbourID, areaID);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                }
                this.register(world, path);
                this.nodeMap.put(roadID, roadNode);
            }
            else if(roadNeighbours.size() >= 3) {
                RouteNode routeNode = this.nodeMap.get(roadID);
                RouteNode roadNode = routeNode != null ? routeNode : new RouteNode(world, roadID);
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
                        if(neighbourList.size() == 2) {
                            neighbourList.remove(neighbourID);
                            path.add(areaID);
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
                                edgeFlag = false;
                                path.add(areaID);
                                routeNode = this.nodeMap.get(areaID);
                                RouteNode node = routeNode != null ? routeNode : new RouteNode(world, areaID);
                                node.addNode(neighbourID, roadID);
                                roadNode.addNode(id, areaID);
                                this.nodeMap.put(areaID, node);
                            }
                        }
                    }
                    this.register(world, path);
                }
                this.nodeMap.put(roadID, roadNode);
            }
            else if(roadNeighbours.size() == 2) {
                if (this.edgeMap.containsKey(roadID)) {
                    continue;
                }
                EntityID start = null;
                List<EntityID> startPath = new ArrayList<>();
                Area area = (Area)world.getEntity(roadNeighbours.get(0));
                EntityID neighbourID = roadID;
                boolean edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbourList = area.getNeighbours();
                    if(neighbourList.size() == 2) {
                        neighbourList.remove(neighbourID);
                        startPath.add(areaID);
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
                            edgeFlag = false;
                            startPath.add(areaID);
                            start = areaID;
                        }
                    }
                }
                EntityID end = null;
                List<EntityID> endPath = new ArrayList<>();
                area = (Area)world.getEntity(roadNeighbours.get(1));
                neighbourID = roadID;
                edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbourList = area.getNeighbours();
                    if(neighbourList.size() == 2) {
                        neighbourList.remove(neighbourID);
                        endPath.add(areaID);
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
                            edgeFlag = false;
                            endPath.add(areaID);
                            end = areaID;
                        }
                    }
                }
                if(start != null && end != null) {
                    List<EntityID> path = new ArrayList<>();
                    Collections.reverse(startPath);
                    path.addAll(startPath);
                    path.add(roadID);
                    path.addAll(endPath);
                    this.register(world, path);
                    RouteNode startNode = this.nodeMap.containsKey(start) ? this.nodeMap.get(start) : new RouteNode(world, start);
                    startNode.addNode(path.get(1), end);
                    this.nodeMap.put(start, startNode);
                    RouteNode endNode = this.nodeMap.containsKey(end) ? this.nodeMap.get(end) : new RouteNode(world, end);
                    endNode.addNode(path.get(path.size() - 2), start);
                    this.nodeMap.put(end, endNode);
                }
            }
        }
    }

    private Set<StandardEntity> analysisBuilding(StandardWorldModel world) {
        //edge road only
        Set<StandardEntity> processedRoad = new HashSet<>();
        //Buildings
        //AmbulanceCentre, FireStation, GasStation, PoliceOffice, Refuge
        for(StandardEntity entity : world.getEntitiesOfType(
                StandardEntityURN.BUILDING,
                StandardEntityURN.REFUGE,
                StandardEntityURN.AMBULANCE_CENTRE,
                StandardEntityURN.FIRE_STATION,
                StandardEntityURN.POLICE_OFFICE,
                StandardEntityURN.GAS_STATION
        )) {
            Building building = (Building)entity;
            EntityID buildingID = building.getID();
            RouteNode routeNode = this.nodeMap.get(buildingID);
            RouteNode buildingNode = routeNode != null ? routeNode : new RouteNode(world, buildingID);
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
                    path.add(areaID);
                    if(area instanceof Building) {
                        edgeFlag = false;
                        routeNode = this.nodeMap.get(areaID);
                        RouteNode node = routeNode != null ? routeNode : new RouteNode(world, areaID);
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
                            RouteNode node = routeNode != null ? routeNode : new RouteNode(world, areaID);
                            node.addNode(neighbourID, buildingID);
                            buildingNode.addNode(id, areaID);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                }
                this.register(world, path);
            }
            this.nodeMap.put(buildingID, buildingNode);
        }
        return processedRoad;
    }

    private void register(StandardWorldModel world, List<EntityID> path) {
        RouteEdge edge = new RouteEdge(world, path);
        int size = path.size() - 1;
        for(int i = 1; i < size; i++) {
            this.edgeMap.put(path.get(i), edge);
        }
        this.edgeTable.put(path.get(0), path.get(size), edge);
        this.edgeTable.put(path.get(size), path.get(0), edge);
    }
}
