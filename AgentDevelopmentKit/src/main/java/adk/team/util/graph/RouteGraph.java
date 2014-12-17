package adk.team.util.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouteGraph {

    private Map<EntityID, RouteNode> nodeMap;

    private Map<EntityID, RouteEdge> edgeMap;

    private Table<EntityID, EntityID, RouteEdge> edgeTable;
    private ConcurrentHashMap<Long, List<EntityID>> cache;
    //private Table<EntityID, EntityID, Set<RouteEdge>> et;

    public RouteGraph(Map<EntityID, RouteNode> nodes, Map<EntityID, RouteEdge> edges, Table<EntityID, EntityID, RouteEdge> connectEdges, ConcurrentHashMap<Long, List<EntityID>> pathCache) {
        this.nodeMap = nodes;
        this.edgeMap = edges;
        this.edgeTable = connectEdges;
        this.cache = pathCache;
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

    public RouteEdge getEdge(RouteNode start, RouteNode end) {
        return this.getEdge(start.getID(), end.getID());
    }

    public RouteEdge getEdge(EntityID start, EntityID end) {
        return this.edgeTable.get(start, end);
    }

    public boolean containsEdge(RouteNode start, RouteNode end) {
        return this.containsEdge(start.getID(), end.getID());
    }

    public boolean containsEdge(EntityID start, EntityID end) {
        return this.edgeTable.contains(start, end);
    }

    public boolean contains(EntityID areaID) {
        return this.nodeMap.containsKey(areaID) || this.edgeMap.containsKey(areaID);
    }

    public List<EntityID> getPath(List<RouteNode> nodes) {
        List<EntityID> path = Lists.newArrayList(nodes.get(0).getID());
        int size = nodes.size() - 1;
        for(int i = 0; i < size; i++) {
            RouteNode node = nodes.get(i);
            RouteNode next = nodes.get(i + 1);
            if(!this.containsEdge(node, next)) {
                return null;
            }
            List<EntityID> edgePath = this.getEdge(node, next).getPath(node);
            if(edgePath != null && !edgePath.isEmpty()) {
                path.addAll(edgePath);
            }
            path.add(next.getID());
        }
        return path;
    }

    public List<EntityID> getPath(RouteNode... nodes) {
        List<EntityID> path = Lists.newArrayList(nodes[0].getID());
        int size = nodes.length - 1;
        for(int i = 0; i < size; i++) {
            RouteNode node = nodes[i];
            RouteNode next = nodes[i + 1];
            if(!this.containsEdge(node, next)) {
                return null;
            }
            List<EntityID> edgePath = this.getEdge(node, next).getPath(node);
            if(edgePath != null && !edgePath.isEmpty()) {
                path.addAll(edgePath);
            }
            path.add(next.getID());
        }
        return path;
    }

    public boolean createPositionNode(StandardWorldModel world, EntityID areaID) {
        if(this.nodeMap.containsKey(areaID)) {
            return true;
        }
        if(this.edgeMap.containsKey(areaID)) {
            RouteEdge edge = this.edgeMap.get(areaID);
            RouteNode node = RouteNode.getInstance(world, areaID);
        /*EntityID first = edge.getFirstNodeID();
        List<EntityID> firstPath = Lists.newArrayList(first);
        firstPath.addAll(edge.getPath(first, areaID));
        EntityID second = edge.getSecondNodeID();
        List<EntityID> secondPath = edge.getPath(areaID, second);
        secondPath.add(second);*/
            List<EntityID> element = edge.getAllElement();

            int index = element.indexOf(areaID);
            List<EntityID> firstPath = element.subList(0, index + 1);
            List<EntityID> secondPath = element.subList(index, element.size());
            this.nodeMap.put(areaID, node);
            if (this.register(world, firstPath) && this.register(world, secondPath)) {
                return true;
            }
        }
        return false;
    }

    private boolean register(StandardWorldModel world, List<EntityID> path) {
        RouteEdge edge = RouteEdge.getInstance(world, path, this.cache);
        if(edge != null) {
            int size = path.size() - 1;
            for (int i = 1; i < size; i++) {
                this.edgeMap.put(path.get(i), edge);
            }
            this.edgeTable.put(path.get(0), path.get(size), edge);
            this.edgeTable.put(path.get(size), path.get(0), edge);
            return true;
        }
        return false;
    }
}
