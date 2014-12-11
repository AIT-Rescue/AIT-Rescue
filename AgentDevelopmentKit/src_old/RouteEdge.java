package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteEdge {
    //path <areaID> (start -> road -> ... -> road -> end)
    private List<EntityID> areas;
    //path cache <startID, targetID, cache>
    private Table<EntityID, EntityID, List<EntityID>> pathTable;
    //node (areas.get(0))
    private EntityID firstNodeID;
    //node (areas.get(areas.size() - 1))
    private EntityID secondNodeID;
    //distance <areaID, distance>
    private Map<EntityID, Double> distanceToTheEndNode;
    private Map<EntityID, Double> roadDistance;

    private Set<EntityID> impassableAreas;

    public RouteEdge(StandardWorldModel world, List<EntityID> path) {
        this.areas = path;
        this.firstNodeID = path.get(0);
        this.secondNodeID = path.get(path.size() - 1);
        this.roadDistance = PositionUtil.getDistanceMap(world, path);
        this.pathTable = HashBasedTable.create();
        this.distanceToTheEndNode = new HashMap<>();
        this.impassableAreas = new HashSet<>();
        this.initDistance();
    }

    private void initDistance() {
        double result = 0.0D;
        for(int i = this.areas.size() - 1; i >= 0; i--) {
            EntityID areaID = this.areas.get(i);
            this.distanceToTheEndNode.put(areaID, result);
            result += this.roadDistance.get(areaID);
        }
    }

    public EntityID getFirstNodeID() {
        return this.firstNodeID;
    }

    public EntityID getSecondNodeID() {
        return this.secondNodeID;
    }

    public boolean contains(EntityID areaID) {
        return this.areas.contains(areaID);
    }

    public boolean isNode(EntityID areaID) {
        return this.isFirstNode(areaID) || this.isSecondNode(areaID);
    }

    public boolean isFirstNode(EntityID areaID) {
        return this.firstNodeID.getValue() == areaID.getValue();
    }

    public boolean isSecondNode(EntityID areaID) {
        return this.secondNodeID.getValue() == areaID.getValue();
    }

    public double getDistance() {
        return this.distanceToTheEndNode.get(this.firstNodeID);
    }

    public double getDistance(EntityID areaID, EntityID target) {
        if(areaID.getValue() == target.getValue()) {
            return Double.NaN;
        }
        if(this.contains(areaID) && this.contains(target)) {
            boolean reverse = this.areas.indexOf(areaID) > this.areas.indexOf(target);
            double start = this.distanceToTheEndNode.get(reverse ? target : areaID);
            double end = this.distanceToTheEndNode.get(reverse ? areaID : target);
            double road = this.roadDistance.get(reverse ? areaID : target);
            return Math.abs(start - end - road);
        }
        return Double.NaN;
    }

    public List<EntityID> getPath(EntityID nodeID) {
        if(this.isFirstNode(nodeID)) {
            if (pathTable.contains(this.firstNodeID, this.secondNodeID)) {
                return this.pathTable.get(this.firstNodeID, this.secondNodeID);
            }
            List<EntityID> result = new ArrayList<>(this.areas);
            result.remove(this.firstNodeID);
            result.remove(this.secondNodeID);
            List<EntityID> reverse = new ArrayList<>(result);
            Collections.reverse(reverse);
            this.pathTable.put(this.firstNodeID, this.secondNodeID, result);
            this.pathTable.put(this.secondNodeID, this.firstNodeID, reverse);
            return result;
        }
        if(this.isSecondNode(nodeID)) {
            if (pathTable.contains(this.secondNodeID, this.firstNodeID)) {
                return this.pathTable.get(this.secondNodeID, this.firstNodeID);
            }
            List<EntityID> reverse = new ArrayList<>(this.areas);
            reverse.remove(this.firstNodeID);
            reverse.remove(this.secondNodeID);
            List<EntityID> result = new ArrayList<>(reverse);
            Collections.reverse(result);
            this.pathTable.put(this.firstNodeID, this.secondNodeID, reverse);
            this.pathTable.put(this.secondNodeID, this.firstNodeID, result);
            return result;
        }
        return null;
    }

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        if(pathTable.contains(nodeID, target)) {
            return this.pathTable.get(nodeID, target);
        }
        if(this.contains(nodeID) && this.contains(target)) {
            List<EntityID> path = new ArrayList<>();
            int start = this.isFirstNode(nodeID) ? 1 : this.isSecondNode(nodeID) ? this.areas.size() - 2 : this.areas.indexOf(nodeID);
            int end = this.isSecondNode(target) ? this.areas.size() - 2 : this.areas.indexOf(target);
            if(start < end) {
                for(int i = start; i <= end; i++) {
                    path.add(this.areas.get(i));
                }
            }
            else {
                for(int i = start; i >= end; i--) {
                    path.add(this.areas.get(i));
                }
            }
            this.pathTable.put(nodeID, target, path);
            List<EntityID> reversePath = new ArrayList<>(path);
            Collections.reverse(reversePath);
            this.pathTable.put(target, nodeID, reversePath);
            return path;
        }
        return null;
    }

    public boolean passable() {
        return this.impassableAreas.isEmpty();
    }

    public void addImpassableArea(EntityID areaID) {
        if(this.areas.contains(areaID)) {
            this.impassableAreas.add(areaID);
        }
    }

    public void removeImpassableArea(EntityID areaID) {
        if(this.areas.contains(areaID)) {
            this.impassableAreas.remove(areaID);
        }
    }
}
