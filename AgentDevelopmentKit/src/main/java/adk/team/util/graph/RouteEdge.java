package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteEdge {
    public final List<EntityID> element;
    //element.get(0);
    public final EntityID firstNodeID;
    //element.get(element.size() - 1);
    public final EntityID secondNodeID;
    //cache
    private List<EntityID> fromFirst;
    private List<EntityID> fromSecond;

    private Set<EntityID> impassableArea;

    private Table<EntityID, EntityID, Double> routeDistance;
    private Map<EntityID, Double> roadDistance;

    public RouteEdge(StandardWorldModel world, List<EntityID> path) {
        this.firstNodeID = path.get(0);
        this.secondNodeID = path.get(path.size() - 1);
        this.element = path;
        this.impassableArea = new HashSet<>();
        this.routeDistance = HashBasedTable.create();
        this.roadDistance = this.getDistanceMap(world, path);
        this.initDistanceToSecond();
        this.createCache();
    }

    public RouteEdge(RouteEdge original) {
        this.firstNodeID = original.firstNodeID;
        this.secondNodeID = original.secondNodeID;
        this.element = original.element;
        this.impassableArea = new HashSet<>(original.getImpassableAreas());
        this.routeDistance = HashBasedTable.create(original.getDistanceTable());
        this.roadDistance = new HashMap<>(original.getDistanceMap());
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
        this.fromFirst = path;
        this.fromSecond = reverse;
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

    public Set<EntityID> getImpassableAreas() {
        return this.impassableArea;
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

    public List<EntityID> getPath(RouteNode from) {
        return this.getPath(from.nodeID);
    }

    public List<EntityID> getPath(EntityID from) {
        if(this.isFirstNode(from)) {
            return this.fromFirst;
        }
        if(this.isSecondNode(from)) {
            return this.fromSecond;
        }
        return null;
    }

    public List<EntityID> getPath(RouteNode node, EntityID target) {
        return this.getPath(node.nodeID, target);
    }

    public List<EntityID> getOldPath(EntityID nodeID, EntityID target) {
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
                for (int i = start; i >= end; i--) {
                    path.add(this.element.get(i));
                }
            }
            return path;
        }
        return null;
    }

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
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
                for (int i = start; i >= end; i--) {
                    path.add(this.element.get(i));
                }
            }
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
        if(!this.isNeighbourNode(edge.firstNodeID) || !this.isNeighbourNode(edge.secondNodeID)) {
            return false;
        }
        List<EntityID> edgeElement = new ArrayList<>(edge.element);
        if(edgeElement.size() != this.element.size()) {
            return false;
        }
        if(edgeElement.get(0).getValue() != this.element.get(0).getValue()) {
            Collections.reverse(edgeElement);
        }
        for(int i = 0; i < edgeElement.size(); i++) {
            if(edgeElement.get(i).getValue() != this.element.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        long value = this.firstNodeID.getValue() < this.secondNodeID.getValue() ? this.getHashKey(this.firstNodeID, this.secondNodeID) : this.getHashKey(this.secondNodeID, this.firstNodeID);
        return Long.hashCode(value);
    }

    public long getHashKey(EntityID rowKey, EntityID columnKey) {
        return ((long)rowKey.getValue() << 32) + (long)columnKey.getValue();
    }

    /*
    public EntityID getFirstNodeID() {
        return this.firstNodeID;
    }

    public EntityID getSecondNodeID() {
        return this.secondNodeID;
    }

    public List<EntityID> getAllElement() {
        return this.element;
    }
    */
}
