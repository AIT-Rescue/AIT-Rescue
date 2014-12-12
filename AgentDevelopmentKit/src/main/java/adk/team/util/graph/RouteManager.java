package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteManager {

    private Map<EntityID, RouteNode> nodeMap;

    private Map<EntityID, RouteEdge> edgeMap;

    private Table<EntityID, EntityID, RouteEdge> edgeTable;

    public RouteManager(StandardWorldModel world) {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.edgeTable = HashBasedTable.create();
        this.analysis(world);
    }

    public RouteGraph getPassableGraph(StandardWorldModel world, EntityID... targets) {
        return null;
    }

    public RouteGraph getPassableGraph(StandardWorldModel world, Collection<EntityID> targets) {
        return null;
    }

    public RouteGraph getGraph(StandardWorldModel world, EntityID position, EntityID... targets) {
        RouteGraph graph = new RouteGraph(new HashMap<>(this.nodeMap), new HashMap<>(this.edgeMap), HashBasedTable.create(this.edgeTable));
        if(position != null) {
            graph.createPositionNode(world, position);
        }
        for(EntityID target : targets) {
            graph.createPositionNode(world, target);
        }
        return graph;
    }

    public RouteGraph getGraph(StandardWorldModel world, EntityID position, Collection<EntityID> targets) {
        RouteGraph graph = new RouteGraph(new HashMap<>(this.nodeMap), new HashMap<>(this.edgeMap), HashBasedTable.create(this.edgeTable));
        if(position != null) {
            graph.createPositionNode(world, position);
        }
        for(EntityID target : targets) {
            graph.createPositionNode(world, target);
        }
        return graph;
    }

    public void setPassable(EntityID areaID, boolean flag) {
        if(this.nodeMap.containsKey(areaID)) {
            RouteNode node = this.nodeMap.get(areaID);
            node.setPassable(flag);
            this.nodeMap.put(areaID, node);
        }
        else if(this.edgeMap.containsKey(areaID)) {
            RouteEdge edge = this.edgeMap.get(areaID);
            edge.setPassable(areaID, flag);
            this.edgeMap.put(areaID, edge);
            EntityID first = edge.getFirstNodeID();
            EntityID second = edge.getSecondNodeID();
            this.edgeTable.put(first, second, edge);
            this.edgeTable.put(second, first, edge);
        }
    }

    private void analysis(StandardWorldModel world) {
        Map<EntityID, Set<EntityID>> processedNeighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        Set<StandardEntity> processedRoad = this.analysisBuilding(world, processedNeighbours);
        Collection<StandardEntity> roads = world.getEntitiesOfType(StandardEntityURN.ROAD, StandardEntityURN.HYDRANT);
        roads.removeAll(processedRoad);
        this.analysisRoad(world, roads, processedNeighbours);
    }

    private void analysisRoad(StandardWorldModel world, Collection<StandardEntity> roads, Map<EntityID, Set<EntityID>> processedNeighbours) {
        Set<StandardEntity> processedRoad = new HashSet<>();
        for(StandardEntity entity : roads) {
            if(processedRoad.contains(entity)) {
                continue;
            }
            Road road = (Road)entity;
            EntityID roadID = road.getID();
            List<EntityID> roadNeighbours = road.getNeighbours();
            if(roadNeighbours.isEmpty()) {
                this.nodeMap.put(roadID, RouteNode.getInstance(world, road));
            }
            else if(roadNeighbours.size() == 1) {
                if(this.nodeMap.containsKey(roadID)) {
                    continue;
                }
                RouteNode roadNode = RouteNode.getInstance(world, road);
                Set<EntityID> processed = processedNeighbours.get(roadID);
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
                            RouteNode node = routeNode != null ? routeNode : RouteNode.getInstance(world, area);
                            node.addNode(roadNode);
                            Set<EntityID> processed1 = processedNeighbours.get(areaID);
                            processed1.add(neighbourID);
                            processedNeighbours.put(areaID, processed1);
                            roadNode.addNode(node);
                            processed.add(roadNeighbourID);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                }
                this.register(world, path);
                this.nodeMap.put(roadID, roadNode);
                processedNeighbours.put(roadID, processed);
            }
            else if(roadNeighbours.size() >= 3) {
                RouteNode routeNode = this.nodeMap.get(roadID);
                RouteNode roadNode = routeNode != null ? routeNode : RouteNode.getInstance(world, road);
                Set<EntityID> processed = processedNeighbours.get(roadID);
                for(EntityID id : roadNeighbours) {
                    if(processed.contains(id)) {
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
                                RouteNode node = routeNode != null ? routeNode : RouteNode.getInstance(world, area);
                                node.addNode(roadNode);
                                Set<EntityID> processed1 = processedNeighbours.get(areaID);
                                processed1.add(neighbourID);
                                processedNeighbours.put(areaID, processed1);
                                roadNode.addNode(node);
                                processed.add(id);
                                this.nodeMap.put(areaID, node);
                            }
                        }
                    }
                    this.register(world, path);
                }
                this.nodeMap.put(roadID, roadNode);
                processedNeighbours.put(roadID, processed);
            }
            else if(roadNeighbours.size() == 2) {
                if (this.edgeMap.containsKey(roadID)) {
                    continue;
                }
                EntityID start = null;
                List<EntityID> startPath = new ArrayList<>();
                Area area = (Area)world.getEntity(roadNeighbours.get(0));
                EntityID neighbourID = roadID;
                Set<EntityID> cache = new HashSet<>();
                boolean edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbourList = area.getNeighbours();
                    if(cache.contains(areaID)) {
                        edgeFlag = false;
                        System.out.println("[ERROR] Bad Map (unknown Area)");
                    }
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
                    cache.add(areaID);
                }
                EntityID end = null;
                List<EntityID> endPath = new ArrayList<>();
                area = (Area)world.getEntity(roadNeighbours.get(1));
                neighbourID = roadID;
                cache = new HashSet<>();
                edgeFlag = true;
                while (edgeFlag) {
                    EntityID areaID = area.getID();
                    List<EntityID> neighbourList = area.getNeighbours();
                    if(cache.contains(areaID)) {
                        edgeFlag = false;
                        System.out.println("[ERROR] Bad Map (unknown Area)");
                    }
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
                    cache.add(areaID);
                }
                if(start != null && end != null) {
                    List<EntityID> path = new ArrayList<>();
                    Collections.reverse(startPath);
                    path.addAll(startPath);
                    path.add(roadID);
                    path.addAll(endPath);
                    this.register(world, path);
                    RouteNode startNode = this.nodeMap.containsKey(start) ? this.nodeMap.get(start) : RouteNode.getInstance(world, start);
                    RouteNode endNode = this.nodeMap.containsKey(end) ? this.nodeMap.get(end) : RouteNode.getInstance(world, end);
                    startNode.addNode(endNode);
                    Set<EntityID> processed = processedNeighbours.get(start);
                    processed.add(path.get(1));
                    processedNeighbours.put(start, processed);
                    this.nodeMap.put(start, startNode);
                    endNode.addNode(startNode);
                    Set<EntityID> processed1 = processedNeighbours.get(end);
                    processed1.add(path.get(path.size() - 2));
                    processedNeighbours.put(end, processed1);
                    this.nodeMap.put(end, endNode);
                }
            }
        }
    }

    private Set<StandardEntity> analysisBuilding(StandardWorldModel world, Map<EntityID, Set<EntityID>> processedNeighbours) {
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
            RouteNode buildingNode = routeNode != null ? routeNode : RouteNode.getInstance(world, building);
            List<EntityID> buildingNeighbours = building.getNeighbours();
            Set<EntityID> processed = processedNeighbours.get(buildingID);
            if(buildingNeighbours.isEmpty()) {
                this.nodeMap.put(buildingID, buildingNode);
                continue;
            }
            for(EntityID id : buildingNeighbours) {
                if(processed.contains(id)) {
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
                        RouteNode node = routeNode != null ? routeNode : RouteNode.getInstance(world, area);
                        node.addNode(buildingNode);
                        Set<EntityID> processed1 = processedNeighbours.get(areaID);
                        processed1.add(neighbourID);
                        processedNeighbours.put(areaID, processed1);
                        buildingNode.addNode(node);
                        processed.add(id);
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
                            RouteNode node = routeNode != null ? routeNode : RouteNode.getInstance(world, area);
                            node.addNode(buildingNode);
                            Set<EntityID> processed1 = processedNeighbours.get(areaID);
                            processed1.add(neighbourID);
                            processedNeighbours.put(areaID, processed1);
                            buildingNode.addNode(node);
                            processed.add(id);
                            this.nodeMap.put(areaID, node);
                        }
                    }
                }
                this.register(world, path);
            }
            this.nodeMap.put(buildingID, buildingNode);
            processedNeighbours.put(buildingID, processed);
        }
        return processedRoad;
    }

    private void register(StandardWorldModel world, List<EntityID> path) {
        RouteEdge edge = RouteEdge.getInstance(world, path);
        int size = path.size() - 1;
        for(int i = 1; i < size; i++) {
            this.edgeMap.put(path.get(i), edge);
        }
        this.edgeTable.put(path.get(0), path.get(size), edge);
        this.edgeTable.put(path.get(size), path.get(0), edge);
    }
}
