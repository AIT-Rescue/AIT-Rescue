package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteEdge {

    private EntityID startNodeID;
    private EntityID endNodeID;
    //start > road > ... > road > end
    private List<EntityID> areas;
    private Map<EntityID, Double> endDistance;
    //道路自体の距離 > 隣接するのは2つのみだから
    private Map<EntityID, Double> roadDistance;

    private Table<EntityID, EntityID, List<EntityID>> pathMap;

    public RouteEdge(StandardWorldModel world, List<EntityID> path) {
        this.areas = path;
        this.startNodeID = path.get(0);
        this.endNodeID = path.get(path.size() - 1);
        this.roadDistance = PositionUtil.getDistanceMap(world, path);
        this.pathMap = HashBasedTable.create();
        this.endDistance = new HashMap<>();
        this.init();
    }

    private void init() {
        this.endDistance.put(this.endNodeID, 0.0D);
        double result = 0.0D;
        for(int i = this.areas.size() - 2; i >= 0; i--) {
            EntityID areaID = this.areas.get(i);
            this.endDistance.put(areaID, result);
            double areaDistance = Math.abs(this.roadDistance.get(areaID));
            this.roadDistance.put(areaID, areaDistance);
            result += areaDistance;
        }
    }

    public double getDistance() {
        return this.endDistance.get(this.startNodeID);
    }

    public double getDistance(EntityID nodeID, EntityID target) {
        if(this.areas.contains(nodeID) && this.areas.contains(target)) {
            double start = this.endDistance.get(nodeID);
            double end = this.endDistance.get(target);
            double road = this.roadDistance.get(target);
            return Math.abs(start - end - road);
        }
        return Double.NaN;
    }

    public List<EntityID> getPath(EntityID nodeID) {
        if(this.startNodeID.getValue() == nodeID.getValue()) {
            if (pathMap.contains(this.startNodeID, this.endNodeID)) {
                return this.pathMap.get(this.startNodeID, this.endNodeID);
            }
            List<EntityID> result = new ArrayList<>(this.areas);
            result.remove(this.startNodeID);
            result.remove(this.endNodeID);
            List<EntityID> reverse = new ArrayList<>(result);
            Collections.reverse(reverse);
            this.pathMap.put(this.startNodeID, this.endNodeID, result);
            this.pathMap.put(this.endNodeID, this.startNodeID, reverse);
            return result;
        }
        if(this.endNodeID.getValue() == nodeID.getValue()) {
            if (pathMap.contains(this.endNodeID, this.startNodeID)) {
                return this.pathMap.get(this.endNodeID, this.startNodeID);
            }
            List<EntityID> reverse = new ArrayList<>(this.areas);
            reverse.remove(this.startNodeID);
            reverse.remove(this.endNodeID);
            List<EntityID> result = new ArrayList<>(reverse);
            Collections.reverse(result);
            this.pathMap.put(this.startNodeID, this.endNodeID, reverse);
            this.pathMap.put(this.endNodeID, this.startNodeID, result);
            return result;
        }
        return null;
    }

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        if(pathMap.contains(nodeID, target)) {
            return this.pathMap.get(nodeID, target);
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
            this.pathMap.put(nodeID, target, path);
            List<EntityID> reversePath = new ArrayList<>(path);
            Collections.reverse(reversePath);
            this.pathMap.put(target, nodeID, reversePath);
            return path;
        }
        return null;
    }
}
