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
    //douro zitai no kyori > mou kimatte irukara
    private Map<EntityID, Double> roadDistance;

    private Table<EntityID, EntityID, List<EntityID>> pathMap;

    public RouteEdge(List<EntityID> path, Map<EntityID, Double> distanceMap) {
        this.areas = path;
        this.startNodeID = path.get(0);
        this.endNodeID = path.get(path.size() - 1);
        this.roadDistance = distanceMap;
        this.roadDistance.put(this.startNodeID, 0.0D);
        this.roadDistance.put(this.endNodeID, 0.0D);
        this.pathMap = HashBasedTable.create();
        this.endDistance = new HashMap<>();
        this.endDistance.put(this.endNodeID, 0.0D);
        double result = 0.0D;
        for(int i = path.size() - 2; i >= 0; i--) {
            EntityID areaID = path.get(i);
            this.endDistance.put(areaID, result);
            double areaDistance = Math.abs(this.roadDistance.get(areaID));
            this.roadDistance.put(areaID, areaDistance);
            result += areaDistance;
        }
    }

    public Map<EntityID, Double> getDistanceMap(StandardWorldModel world, List<EntityID> path) {
        Map<EntityID, Double> result = new HashMap<>();
        int size = path.size() - 2;
        for(int i = 1; i < size; i++) {
            EntityID areaID = path.get(i);
            Area area = (Area)world.getEntity(areaID);
            double distance = PositionUtil.pointDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
            result.put(areaID, distance);
        }
        return result;
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

    public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        List<EntityID> path = new ArrayList<>();
        int start;
        int end;
        if(pathMap.contains(nodeID, target)) {
            return this.pathMap.get(nodeID, target);
        }
        if(this.areas.contains(nodeID) && this.areas.contains(target)) {
            int sValue = this.startNodeID.getValue();
            int eValue = this.endNodeID.getValue();
            int nValue = nodeID.getValue();
            int tValue = target.getValue();
            start = (sValue == nValue) ? 1 : (eValue == nValue) ? areas.size() - 2 : areas.indexOf(nodeID);
            end = (eValue == tValue) ? areas.size() - 2 : (sValue == tValue) ? 1 : areas.indexOf(target);
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

    /*
    private Map<EntityID, Table<EntityID, EntityID, Double>> distanceMap;

    private double edgeDistance = Double.NaN;

    public double getDistance() {
        return this.edgeDistance;
    }

    public double getDistance(EntityID nodeID, EntityID target) {
        if(nodeID.getValue() == this.startNodeID.getValue()) {
            if(target.getValue() == this.endNodeID.getValue()) {
                if(!Double.isNaN(this.edgeDistance)) {
                    return this.edgeDistance;
                }
            }
            double result = 0.0D;
            int size = roads.size() - 1;
            for(int i = 1; i < size; i++) {
                EntityID roadID = roads.get(i);
                if(roadID.getValue() == target.getValue()) {
                    return result;
                }
            }
        }
        else if(nodeID.getValue() == this.endNodeID.getValue()) {
            if(target.getValue() == this.startNodeID.getValue()) {
                return this.edgeDistance;
            }
        }
            return Double.NaN;
    }
    */
}
