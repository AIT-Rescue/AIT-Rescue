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
    private EntityID startNodeID;
    //node (areas.get(areas.size() - 1))
    private EntityID endNodeID;
    //distance <areaID, distance>
    private Map<EntityID, Double> distanceToTheEndNode;
    private Map<EntityID, Double> roadDistance;

    public RouteEdge(StandardWorldModel world, List<EntityID> path) {
        this.areas = path;
        this.startNodeID = path.get(0);
        this.endNodeID = path.get(path.size() - 1);
        this.roadDistance = PositionUtil.getDistanceMap(world, path);
        this.pathTable = HashBasedTable.create();
        this.distanceToTheEndNode = new HashMap<>();
        this.init();
    }

    private void init() {
        this.distanceToTheEndNode.put(this.endNodeID, 0.0D);
        double result = 0.0D;
        for(int i = this.areas.size() - 2; i >= 0; i--) {
            EntityID areaID = this.areas.get(i);
            this.distanceToTheEndNode.put(areaID, result);
            result += this.roadDistance.get(areaID);
        }
    }

    public EntityID getStartNodeID() {
        return this.startNodeID;
    }

    public EntityID getEndNodeID() {
        return this.endNodeID;
    }

    public double getDistance() {
        return this.distanceToTheEndNode.get(this.startNodeID);
    }

    public double getDistance(EntityID areaID, EntityID target) {
        if(areaID.getValue() == target.getValue()) {
            return Double.NaN;
        }
        if(this.areas.contains(areaID) && this.areas.contains(target)) {
            boolean reverse = this.areas.indexOf(areaID) > this.areas.indexOf(target);
            double start = this.distanceToTheEndNode.get(reverse ? target : areaID);
            double end = this.distanceToTheEndNode.get(reverse ? areaID : target);
            double road = this.roadDistance.get(reverse ? areaID : target);
            return Math.abs(start - end - road);
        }
        return Double.NaN;
    }

    public List<EntityID> getPath(EntityID nodeID) {
        if(this.startNodeID.getValue() == nodeID.getValue()) {
            if (pathTable.contains(this.startNodeID, this.endNodeID)) {
                return this.pathTable.get(this.startNodeID, this.endNodeID);
            }
            List<EntityID> result = new ArrayList<>(this.areas);
            result.remove(this.startNodeID);
            result.remove(this.endNodeID);
            List<EntityID> reverse = new ArrayList<>(result);
            Collections.reverse(reverse);
            this.pathTable.put(this.startNodeID, this.endNodeID, result);
            this.pathTable.put(this.endNodeID, this.startNodeID, reverse);
            return result;
        }
        if(this.endNodeID.getValue() == nodeID.getValue()) {
            if (pathTable.contains(this.endNodeID, this.startNodeID)) {
                return this.pathTable.get(this.endNodeID, this.startNodeID);
            }
            List<EntityID> reverse = new ArrayList<>(this.areas);
            reverse.remove(this.startNodeID);
            reverse.remove(this.endNodeID);
            List<EntityID> result = new ArrayList<>(reverse);
            Collections.reverse(result);
            this.pathTable.put(this.startNodeID, this.endNodeID, reverse);
            this.pathTable.put(this.endNodeID, this.startNodeID, result);
            return result;
        }
        return null;
    }

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        if(pathTable.contains(nodeID, target)) {
            return this.pathTable.get(nodeID, target);
        }
        if(this.areas.contains(nodeID) && this.areas.contains(target)) {
            List<EntityID> path = new ArrayList<>();
            int sValue = this.startNodeID.getValue();
            int eValue = this.endNodeID.getValue();
            int nValue = nodeID.getValue();
            int tValue = target.getValue();
            int start = (sValue == nValue) ? 1 : (eValue == nValue) ? areas.size() - 2 : areas.indexOf(nodeID);
            int end = (eValue == tValue) ? areas.size() - 2 : (sValue == tValue) ? 1 : areas.indexOf(target);
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
}
