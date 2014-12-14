package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteManager {
    // All Element
    private Map<EntityID, RouteNode> nodeMap;
    private Map<EntityID, RouteEdge> edgeMap;
    private Table<EntityID, EntityID, RouteEdge> edgeTable;
    // Passable Element
    private Map<EntityID, RouteNode> passableNodeMap;
    private Map<EntityID, RouteEdge> passableEdgeMap;
    private Table<EntityID, EntityID, RouteEdge> passableEdgeTable;

    public RouteManager(StandardWorldModel world) {
        this.nodeMap = new HashMap<>();
        this.edgeMap = new HashMap<>();
        this.edgeTable = HashBasedTable.create();
        this.analysis(world);
    }

    public RouteGraph getPassableGraph(StandardWorldModel world) {
        return new RouteGraph(new HashMap<>(this.passableNodeMap), new HashMap<>(this.passableEdgeMap), HashBasedTable.create(this.passableEdgeTable));
    }

    public RouteGraph getGraph() {
        return new RouteGraph(new HashMap<>(this.nodeMap), new HashMap<>(this.edgeMap), HashBasedTable.create(this.edgeTable));
    }

    public void setPassable(StandardWorldModel world, EntityID areaID, boolean flag) {
        if(this.nodeMap.containsKey(areaID)) {
            RouteNode node = this.nodeMap.get(areaID);
            node.setPassable(flag);
            this.nodeMap.put(areaID, node);
            if(flag) {
                this.addNode(world, areaID);
            }
            else {
                this.removeNode(world, areaID);
            }
        }
        else if(this.edgeMap.containsKey(areaID)) {
            RouteEdge edge = this.edgeMap.get(areaID);
            edge.setPassable(areaID, flag);
            this.edgeMap.put(areaID, edge);
            EntityID first = edge.getFirstNodeID();
            EntityID second = edge.getSecondNodeID();
            this.edgeTable.put(first, second, edge);
            this.edgeTable.put(second, first, edge);
            if(flag) {
                this.addEdge(world, areaID);
            }
            else {
                this.removeEdge(world, areaID);
            }
        }
    }

    private Set<EntityID> impassableArea;

    private void registerPassable(StandardWorldModel world, List<EntityID> path) {

        RouteEdge edge = RouteEdge.getInstance(world, path);
        int size = path.size() - 1;
        for(int i = 1; i < size; i++) {
            this.passableEdgeMap.put(path.get(i), edge);
        }
        this.passableEdgeTable.put(path.get(0), path.get(size), edge);
        this.passableEdgeTable.put(path.get(size), path.get(0), edge);
    }

    // remove node
    private void removeNode(StandardWorldModel world, EntityID areaID) {
        if(this.impassableArea.contains(areaID)) {
            return;
        }
        this.impassableArea.add(areaID);
        // create newNode
        RouteNode oldNode = this.passableNodeMap.get(areaID);
        if(oldNode == null) {
            return;
        }
        for(EntityID nodeID : ((Area)world.getEntity(areaID)).getNeighbours()) {
            if(this.passableNodeMap.containsKey(nodeID)) {
                RouteNode node = this.passableNodeMap.get(nodeID);
                node.removeNode(oldNode);
                this.passableNodeMap.put(nodeID, node);
                continue;
            }
            // create new Node
            RouteEdge edge = this.passableEdgeMap.get(nodeID);
            EntityID another = edge.getAnotherNodeID(areaID);
            RouteNode newNode = RouteNode.getInstance(world, nodeID);
            RouteNode anotherNode = this.passableNodeMap.get(another);
            // create new route
            List<EntityID> newPath = edge.getPath(nodeID, another);
            newPath.add(another);
            this.registerPassable(world, newPath);
            // remove old data
            newNode.addNode(anotherNode);
            anotherNode.addNode(newNode);
            anotherNode.removeNode(oldNode);
            this.passableEdgeMap.remove(nodeID);
            this.passableEdgeTable.remove(areaID, another);
            this.passableEdgeTable.remove(another, areaID);
            this.passableNodeMap.put(nodeID, newNode);
            this.passableNodeMap.put(another, anotherNode);
        }
        this.passableNodeMap.remove(areaID);
    }

    // remove edge
    private void removeEdge(StandardWorldModel world, EntityID areaID) {
        if(this.impassableArea.contains(areaID)) {
            return;
        }
        this.impassableArea.add(areaID);
        RouteEdge edge = this.passableEdgeMap.get(areaID);
        List<EntityID> path = edge.getAllElement();
        int areaIndex = path.indexOf(areaID);
        EntityID firstNeighbour = path.get(areaIndex - 1);
        EntityID secondNeighbour = path.get(areaIndex + 1);
        if(this.passableNodeMap.containsKey(firstNeighbour)) {
            RouteNode node = this.passableNodeMap.get(firstNeighbour);
            node.removeNode(edge.getAnotherNodeID(firstNeighbour));
            this.passableNodeMap.put(firstNeighbour, node);
        }
        else {
            RouteNode node = RouteNode.getInstance(world, firstNeighbour);
            EntityID firstNodeID = edge.getFirstNodeID();
            RouteNode firstNode = this.passableNodeMap.get(firstNodeID);
            node.addNode(firstNode);
            firstNode.addNode(node);
            firstNode.removeNode(edge.getSecondNodeID());
            this.registerPassable(world, path.subList(0, areaIndex));
            this.passableEdgeMap.remove(firstNeighbour);
            this.passableNodeMap.put(firstNeighbour, node);
            this.passableNodeMap.put(firstNodeID, firstNode);
        }

        if(this.passableNodeMap.containsKey(secondNeighbour)) {
            RouteNode node = this.passableNodeMap.get(secondNeighbour);
            node.removeNode(edge.getAnotherNodeID(secondNeighbour));
            this.passableNodeMap.put(secondNeighbour, node);
        }
        else {
            RouteNode node = RouteNode.getInstance(world, secondNeighbour);
            EntityID secondNodeID = edge.getSecondNodeID();
            RouteNode secondNode = this.passableNodeMap.get(secondNodeID);
            node.addNode(secondNode);
            secondNode.addNode(node);
            secondNode.removeNode(edge.getFirstNodeID());
            this.registerPassable(world, path.subList(areaIndex + 1, path.size()));
            this.passableEdgeMap.remove(secondNeighbour);
            this.passableNodeMap.put(secondNeighbour, node);
            this.passableNodeMap.put(secondNodeID, secondNode);
        }
        this.passableEdgeMap.remove(areaID);
    }

    // add node
    private void addNode(StandardWorldModel world, EntityID areaID) {
        if(!this.impassableArea.contains(areaID)) {
            return;
        }
        this.impassableArea.remove(areaID);
        RouteNode node = new RouteNode(this.nodeMap.get(areaID));
        for(EntityID nodeID : ((Area)world.getEntity(areaID)).getNeighbours()) {
            if(!this.passableNodeMap.containsKey(nodeID)) {
                node.removeNode(nodeID);
                continue;
            }
            if(this.nodeMap.containsKey(nodeID)) {
                RouteNode neighbourNode = this.passableNodeMap.get(nodeID);
                neighbourNode.addNode(areaID);
                this.passableNodeMap.put(nodeID, neighbourNode);
                continue;
            }
            this.passableNodeMap.get(nodeID).getNeighbours().stream().filter(id -> id.getValue() != areaID.getValue()).forEach(id -> {
                if(this.passableNodeMap.containsKey(id)) {
                    this.registerPassable(world, Lists.newArrayList(areaID, nodeID, id));
                    RouteNode anotherNode = this.passableNodeMap.get(id);
                    anotherNode.removeNode(nodeID);
                    anotherNode.addNode(areaID);
                    node.addNode(id);
                    this.passableNodeMap.put(id, anotherNode);
                    this.passableNodeMap.remove(nodeID);
                }
                else {
                    RouteEdge edge = this.passableEdgeMap.get(id);
                    EntityID anotherID = edge.getAnotherNodeID(nodeID);
                    List<EntityID> path = Lists.newArrayList(areaID, nodeID);
                    path.addAll(edge.getPath(nodeID));
                    path.add(anotherID);
                    this.registerPassable(world, path);
                    RouteNode anotherNode = this.passableNodeMap.get(anotherID);
                    anotherNode.removeNode(nodeID);
                    anotherNode.addNode(areaID);
                    node.addNode(anotherID);
                    this.passableNodeMap.put(anotherID, anotherNode);
                    this.passableNodeMap.remove(nodeID);
                }
            });
        }
        this.passableNodeMap.put(areaID, node);
    }

    // add edge
    private void addEdge(StandardWorldModel world, EntityID areaID) {
        if(!this.impassableArea.contains(areaID)) {
            return;
        }
        this.impassableArea.remove(areaID);
        /*RouteEdge edge = this.edgeMap.get(areaID);
        List<EntityID> path = edge.getAllElement();
        if(path.size() == 3) {
            EntityID firstID = path.get(0);
            EntityID secondID = path.get(2);
            RouteNode first = this.passableNodeMap.get(firstID);
            RouteNode second = this.passableNodeMap.get(secondID);
            if(first != null && second != null) {
                this.passableEdgeMap.put(areaID, edge);
                first.addNode(second);
                second.addNode(first);
                this.passableNodeMap.put(firstID, first);
                this.passableNodeMap.put(secondID, second);
                this.passableEdgeTable.put(firstID, secondID, edge);
                this.passableEdgeTable.put(secondID, firstID, edge);
            }
            else if(first != null) {

            }
            else if(second != null) {

            }
            return;
        }
        int areaIndex = path.indexOf(areaID);
        List<EntityID> newPath = new ArrayList<>();
        if(this.passableNodeMap.containsKey(path.get(areaIndex - 2))) {

        }
        else {
            RouteEdge firstEdge = this.passableEdgeMap.get(path.get(areaIndex - 2));
            boolean reverse = firstEdge.isFirstNode(path.get(areaIndex - 1));
            newPath.add(reverse ? firstEdge.getSecondNodeID() : firstEdge.getFirstNodeID());
            newPath.addAll(firstEdge.getPath(firstEdge.getFirstNodeID()));
            newPath.add(firstEdge.getSecondNodeID());
        }
        newPath.add(areaID);
        RouteEdge secondEdge = this.passableEdgeMap.get(path.get(areaIndex + 2));
        EntityID next = secondEdge.isFirstNode(path.get(areaIndex + 1)) ? secondEdge.getFirstNodeID() : secondEdge.getSecondNodeID();
        newPath.add(next);
        */
        /*int size = path.size() - 1;
        for(int i = 1; i < size; i++) {
            this.edgeMap.put(path.get(i), edge);
        }
        this.edgeTable.put(path.get(0), path.get(size), edge);
        this.edgeTable.put(path.get(size), path.get(0), edge);*/
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
        this.passableNodeMap = new HashMap<>(this.nodeMap);
        this.passableEdgeMap = new HashMap<>(this.edgeMap);
        this.passableEdgeTable = HashBasedTable.create(this.edgeTable);
        this.impassableArea = new HashSet<>();
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
