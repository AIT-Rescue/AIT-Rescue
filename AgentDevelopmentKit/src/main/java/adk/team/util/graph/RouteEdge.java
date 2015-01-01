package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RouteEdge {

    private List<EntityID> element;
    private ConcurrentHashMap<Long, List<EntityID>> cache;
    private List<EntityID> fromFirst;
    private List<EntityID> fromSecond;

    public final EntityID firstNodeID;
    public final EntityID secondNodeID;

    private Set<EntityID> impassableArea;

    private Table<EntityID, EntityID, Double> routeDistance;
    private Map<EntityID, Double> roadDistance;

    private RouteEdge(StandardWorldModel world, List<EntityID> path, ConcurrentHashMap<Long, List<EntityID>> pathCache) {
        this.firstNodeID = path.get(0);
        this.secondNodeID = path.get(path.size() - 1);
        this.element = path;
        this.impassableArea = new HashSet<>();
        this.cache = pathCache;
        this.routeDistance = HashBasedTable.create();
        this.roadDistance = this.getDistanceMap(world, path);
        this.initDistanceToSecond();
        this.createCache();
    }

    private RouteEdge(RouteEdge original) {
        this.firstNodeID = original.getFirstNodeID();
        this.secondNodeID = original.getSecondNodeID();
        this.element = original.getAllElement();
        this.impassableArea = new HashSet<>(original.getImpassableAreas());
        this.cache = original.getCache();
        this.routeDistance = HashBasedTable.create(original.getDistanceTable());
        this.roadDistance = new HashMap<>(original.getDistanceMap());
    }

    public static RouteEdge getInstance(StandardWorldModel world, List<EntityID> path, ConcurrentHashMap<Long, List<EntityID>> pathCache) {
        if(world != null && path != null && path.size() > 1) {
            return new RouteEdge(world, path, pathCache);
        }
        return null;
    }

    public static RouteEdge copy(RouteEdge original) {
        return original != null ? new RouteEdge(original) : null;
    }

    private void initDistanceToSecond() {
        double result = 0.0D;
        for(int i = this.element.size() - 2; i >= 0; i--) {
            EntityID areaID = this.element.get(i);
            this.routeDistance.put(areaID, this.secondNodeID, result);
            this.routeDistance.put(this.secondNodeID, areaID, result);
            result += this.roadDistance.get(areaID);
        }
    }

    private void createCache() {
        List<EntityID> path = new ArrayList<>(this.element);
        path.remove(this.firstNodeID);
        path.remove(this.secondNodeID);
        List<EntityID> reverse = new ArrayList<>(path);
        Collections.reverse(reverse);
        this.cache.put(getCacheKey(this.firstNodeID, this.secondNodeID), path);
        this.cache.put(getCacheKey(this.secondNodeID, this.firstNodeID), reverse);
    }

    public EntityID getFirstNodeID() {
        return this.firstNodeID;
    }

    public EntityID getSecondNodeID() {
        return this.secondNodeID;
    }

    public EntityID getAnotherNodeID(EntityID nodeID) {
        if(this.firstNodeID.getValue() == nodeID.getValue()) {
            return this.secondNodeID;
        }
        if(this.secondNodeID.getValue() == nodeID.getValue()) {
            return this.firstNodeID;
        }
        return null;
    }

    public boolean isNeighbourNode(RouteNode node) {
        return this.isNeighbourNode(node.nodeID);
    }

    public boolean isNeighbourNode(EntityID nodeID) {
        return this.isFirstNode(nodeID) || this.isSecondNode(nodeID);
    }

    public boolean isFirstNode(RouteNode node) {
        return this.isFirstNode(node.nodeID);
    }

    public boolean isFirstNode(EntityID nodeID) {
        return this.firstNodeID.getValue() == nodeID.getValue();
    }

    public boolean isSecondNode(RouteNode node) {
        return this.isSecondNode(node.nodeID);
    }

    public boolean isSecondNode(EntityID nodeID) {
        return this.secondNodeID.getValue() == nodeID.getValue();
    }

    public boolean isEdgeElement(EntityID areaID) {
        return this.element.contains(areaID);
    }

    public List<EntityID> getAllElement() {
        return this.element;
    }

    public Set<EntityID> getImpassableAreas() {
        return this.impassableArea;
    }

    protected ConcurrentHashMap<Long, List<EntityID>> getCache() {
        return this.cache;
    }

    protected Table<EntityID, EntityID, Double> getDistanceTable() {
        return  this.routeDistance;
    }

    protected Map<EntityID, Double> getDistanceMap() {
        return this.roadDistance;
    }

    public boolean passable() {
        return this.impassableArea.isEmpty();
    }

    public void setPassable(EntityID areaID, boolean flag) {
        if(flag) {
            this.impassableArea.add(areaID);
        }
        else {
            this.impassableArea.remove(areaID);
        }
    }

    public double getDistance() {
        return this.routeDistance.get(this.firstNodeID, this.secondNodeID);
    }

    public double getDistance(EntityID areaID, EntityID target) {
        if(areaID.getValue() == target.getValue()) {
            return 0.0D;
        }
        if(this.routeDistance.contains(areaID, target)) {
            return this.routeDistance.get(areaID, target);
        }
        if(this.isEdgeElement(areaID) && this.isEdgeElement(target)) {
            boolean reverse = this.element.indexOf(areaID) > this.element.indexOf(target);
            double start = this.routeDistance.get(reverse ? target : areaID, this.secondNodeID);
            double end = this.routeDistance.get(reverse ? areaID : target, this.secondNodeID);
            double road = this.roadDistance.get(reverse ? areaID : target);
            double distance = Math.abs(start - end - road);
            this.routeDistance.put(areaID, target, distance);
            this.routeDistance.put(target, areaID, distance);
            return distance;
        }
        return Double.NaN;
    }

    public List<EntityID> getPath(RouteNode node) {
        return this.getPath(node.nodeID);
    }

    public List<EntityID> getPath(EntityID nodeID) {
        if(this.isFirstNode(nodeID)) {
            return this.cache.get(this.getCacheKey(this.firstNodeID, this.secondNodeID));
        }
        if(this.isSecondNode(nodeID)) {
            return this.cache.get(this.getCacheKey(this.secondNodeID, this.firstNodeID));
        }
        return null;
    }

    public List<EntityID> getPath(RouteNode node, EntityID target) {
        return this.getPath(node.nodeID, target);
    }

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        if(this.cache.containsKey(this.getCacheKey(nodeID, target))) {
            return this.cache.get(this.getCacheKey(nodeID, target));
        }
        if(this.isEdgeElement(nodeID) && this.isEdgeElement(target)) {
            List<EntityID> path = new ArrayList<>();
            int start = this.isFirstNode(nodeID) ? 1 : this.isSecondNode(nodeID) ? this.element.size() - 2 : this.element.indexOf(nodeID);
            int end = this.isSecondNode(target) ? this.element.size() - 2 : this.isFirstNode(nodeID) ? 1 : this.element.indexOf(target);
            if(start < end) {
                for(int i = start; i <= end; i++) {
                    path.add(this.element.get(i));
                }
            }
            else {
                for(int i = start; i >= end; i--) {
                    path.add(this.element.get(i));
                }
            }
            this.cache.put(this.getCacheKey(nodeID, target), path);
            List<EntityID> reversePath = new ArrayList<>(path);
            Collections.reverse(reversePath);
            this.cache.put(this.getCacheKey(target, nodeID), reversePath);
            return path;
        }
        return null;
    }

    protected Map<EntityID, Double> getDistanceMap(StandardWorldModel world, List<EntityID> path) {
        Map<EntityID, Double> result = new HashMap<>();
        result.put(path.get(0), 0.0D);
        int size = path.size() - 1;
        result.put(path.get(size), 0.0D);
        for(int i = 1; i < size; i++) {
            EntityID areaID = path.get(i);
            Area area = (Area)world.getEntity(areaID);
            double distance = PositionUtil.getDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
            result.put(areaID, distance);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RouteEdge && this.equals((RouteEdge)o);
    }

    public boolean equals(RouteEdge edge) {
        if(!this.isNeighbourNode(edge.getFirstNodeID()) || !this.isNeighbourNode(edge.getSecondNodeID())) {
            return false;
        }
        List<EntityID> element = edge.getAllElement();
        if(element.size() != this.element.size()) {
            return false;
        }
        if(element.get(0).getValue() != this.element.get(0).getValue()) {
            Collections.reverse(element);
        }
        for(int i = 0; i < element.size(); i++) {
            if(element.get(i).getValue() != this.element.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }

    public long getCacheKey(EntityID rowKey, EntityID columnKey) {
        return (((long)rowKey.getValue()) << 32) + ((long)columnKey.getValue());
    }
}
