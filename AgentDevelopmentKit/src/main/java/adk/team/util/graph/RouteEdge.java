package adk.team.util.graph;

import com.google.common.collect.Table;
import rescuecore2.worldmodel.EntityID;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

public class RouteEdge {

    /*public List<EntityID> getRoads();

    public double getDistance();

    public double getDistance(EntityID roadID);
    */
    private EntityID startNodeID;
    private EntityID endNodeID;

    //start > road > ... > road > end
    private List<EntityID> roads;

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


}
