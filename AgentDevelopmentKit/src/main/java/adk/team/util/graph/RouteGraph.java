package adk.team.util.graph;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.List;
import java.util.Map;

public class RouteGraph {

    private Map<EntityID, RouteNode> nodeMap;

    private Map<EntityID, RouteEdge> edgeMap;

    private Table<EntityID, EntityID, RouteEdge> edgeTable;

    public RouteGraph(Map<EntityID, RouteNode> nodes, Map<EntityID, RouteEdge> edges, Table<EntityID, EntityID, RouteEdge> connectEdges) {
        this.nodeMap = nodes;
        this.edgeMap = edges;
        this.edgeTable = connectEdges;
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
        int count = 0;
        int size = nodes.size() - 1;
        while(count < size) {
            RouteNode node = nodes.get(count);
            count++;
            RouteNode next = nodes.get(count);
            if(!this.containsEdge(node, next)) {
                return null;
            }
            path.addAll(this.edgeTable.get(node, next).getPath(node));
            path.add(next.getID());
        }
        return path;
    }

    public List<EntityID> getPath(RouteNode... nodes) {
        List<EntityID> path = Lists.newArrayList(nodes[0].getID());
        int count = 0;
        int size = nodes.length - 1;
        while(count < size) {
            RouteNode node = nodes[count];
            count++;
            RouteNode next = nodes[count];
            if(!this.containsEdge(node, next)) {
                return null;
            }
            path.addAll(this.edgeTable.get(node, next).getPath(node));
            path.add(next.getID());
        }
        return path;
    }

    public boolean createPositionNode(StandardWorldModel world, EntityID areaID) {
        if(this.nodeMap.containsKey(areaID)) {
            return true;
        }
        if(!this.edgeMap.containsKey(areaID)) {
            return false;
        }
        RouteEdge edge = this.edgeMap.get(areaID);
        RouteNode node = RouteNode.getInstance(world, areaID);
        EntityID first = edge.getFirstNodeID();
        List<EntityID> firstPath = Lists.newArrayList(first);
        firstPath.addAll(edge.getPath(first, areaID));
        EntityID second = edge.getSecondNodeID();
        List<EntityID> secondPath = edge.getPath(areaID, second);
        secondPath.add(second);
        this.nodeMap.put(areaID, node);
        this.register(world, firstPath);
        this.register(world, secondPath);
        return true;
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
