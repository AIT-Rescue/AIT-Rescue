package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteEdge {

    private List<EntityID> route;
    // RouteManagerに移動
    private Cache<List<EntityID>> cache;

    private EntityID firstNodeID;
    private EntityID secondNodeID;

    private Set<EntityID> impassable;

    private Table<EntityID, EntityID, Double> routeDistance;
    private Map<EntityID, Double> roadDistance;

    public RouteEdge(RouteEdge original) {
        this.firstNodeID = original.getFirstNodeID();
        this.secondNodeID = original.getSecondNodeID();
        this.route = original.getAllElement();
        this.impassable = new HashSet<>(original.getImpassable());
        this.cache = original.getCache();
        this.routeDistance = HashBasedTable.create(original.getDistanceTable());
        this.roadDistance = new HashMap<>(original.getDistanceMap());
    }

    private RouteEdge(StandardWorldModel world, List<EntityID> path, Cache<List<EntityID>> pathCache) {
        this.firstNodeID = path.get(0);
        this.secondNodeID = path.get(path.size() - 1);
        this.route = path;
        this.impassable = new HashSet<>();
        this.cache = pathCache;
        this.routeDistance = HashBasedTable.create();
        this.roadDistance = this.getDistanceMap(world, path);
        this.initDistanceToSecond();
        this.createCache();
    }

    public static RouteEdge getInstance(StandardWorldModel world, List<EntityID> path, Cache<List<EntityID>> cache) {
        if(world != null && path != null && path.size() > 1) {
            return new RouteEdge(world, path, cache);
        }
        return null;
    }

    private void initDistanceToSecond() {
        double result = 0.0D;
        for(int i = this.route.size() - 2; i >= 0; i--) {
            EntityID areaID = this.route.get(i);
            this.routeDistance.put(areaID, this.secondNodeID, result);
            this.routeDistance.put(this.secondNodeID, areaID, result);
            result += this.roadDistance.get(areaID);
        }
    }

    private void createCache() {
        List<EntityID> path = new ArrayList<>(this.route);
        path.remove(this.firstNodeID);
        path.remove(this.secondNodeID);
        List<EntityID> reverse = new ArrayList<>(path);
        Collections.reverse(reverse);
        this.cache.put(this.firstNodeID, this.secondNodeID, path);
        this.cache.put(this.secondNodeID, this.firstNodeID, reverse);
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
        return this.isNeighbourNode(node.getID());
    }

    public boolean isNeighbourNode(EntityID nodeID) {
        return this.isFirstNode(nodeID) || this.isSecondNode(nodeID);
    }

    public boolean isFirstNode(RouteNode node) {
        return this.isFirstNode(node.getID());
    }

    public boolean isFirstNode(EntityID nodeID) {
        return this.firstNodeID.getValue() == nodeID.getValue();
    }

    public boolean isSecondNode(RouteNode node) {
        return this.isSecondNode(node.getID());
    }

    public boolean isSecondNode(EntityID nodeID) {
        return this.secondNodeID.getValue() == nodeID.getValue();
    }

    public boolean isEdgeElement(EntityID areaID) {
        return this.route.contains(areaID);
    }

    public List<EntityID> getAllElement() {
        return this.route;
    }

    public Set<EntityID> getImpassable() {
        return this.impassable;
    }

    public Cache<List<EntityID>> getCache() {
        return this.cache;
    }

    public Table<EntityID, EntityID, Double> getDistanceTable() {
        return  this.routeDistance;
    }

    public Map<EntityID, Double> getDistanceMap() {
        return this.roadDistance;
    }

    public boolean passable() {
        return this.impassable.isEmpty();
    }

    public void setPassable(EntityID areaID, boolean flag) {
        if(flag) {
            this.impassable.add(areaID);
        }
        else {
            this.impassable.remove(areaID);
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
            boolean reverse = this.route.indexOf(areaID) > this.route.indexOf(target);
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
        return this.getPath(node.getID());
    }

    public List<EntityID> getPath(EntityID nodeID) {
        if(this.isFirstNode(nodeID)) {
            return this.cache.get(this.firstNodeID, this.secondNodeID);
        }
        if(this.isSecondNode(nodeID)) {
            return this.cache.get(this.secondNodeID, this.firstNodeID);
        }
        return null;
    }

    public List<EntityID> getPath(RouteNode node, EntityID target) {
        return this.getPath(node.getID(), target);
    }

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        if(this.cache.contains(nodeID, target)) {
            return this.cache.get(nodeID, target);
        }
        if(this.isEdgeElement(nodeID) && this.isEdgeElement(target)) {
            List<EntityID> path = new ArrayList<>();
            int start = this.isFirstNode(nodeID) ? 1 : this.isSecondNode(nodeID) ? this.route.size() - 2 : this.route.indexOf(nodeID);
            int end = this.isSecondNode(target) ? this.route.size() - 2 : this.isFirstNode(nodeID) ? 1 : this.route.indexOf(target);
            if(start < end) {
                for(int i = start; i <= end; i++) {
                    path.add(this.route.get(i));
                }
            }
            else {
                for(int i = start; i >= end; i--) {
                    path.add(this.route.get(i));
                }
            }
            this.cache.put(nodeID, target, path);
            List<EntityID> reversePath = new ArrayList<>(path);
            Collections.reverse(reversePath);
            this.cache.put(target, nodeID, reversePath);
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
            double distance = PositionUtil.pointDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
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
        if(element.size() != this.route.size()) {
            return false;
        }
        if(element.get(0).getValue() != this.route.get(0).getValue()) {
            Collections.reverse(element);
        }
        for(int i = 0; i < element.size(); i++) {
            if(element.get(i).getValue() != this.route.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }
}
