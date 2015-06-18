package adk.sample.basic.tactics;

import adk.team.util.graph.PositionUtil;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointSelector {

    private StandardWorldModel world;

    public Map<EntityID, List<List<Edge>>> neighbourEdgesMap;
    public Map<EntityID, Map<EntityID, Point2D>> passablePointMap;

    public PointSelector(StandardWorldModel standardWorldModel) {
        this.world = standardWorldModel;
        this.neighbourEdgesMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
        this.init();
    }

    private void init() {
        for(StandardEntity entity : this.world.getEntitiesOfType(
                StandardEntityURN.BUILDING,
                StandardEntityURN.AMBULANCE_CENTRE,
                StandardEntityURN.FIRE_STATION,
                StandardEntityURN.POLICE_OFFICE,
                StandardEntityURN.REFUGE,
                StandardEntityURN.GAS_STATION,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.ROAD
        )) {
            this.analysisArea((Area)entity);
        }
    }

    public void analysisArea(Area area) {
        EntityID roadID = area.getID();
        if (this.passablePointMap.containsKey(roadID)) {
            return;
        }
        List<List<Edge>> neighbourEdges = new ArrayList<>();
        Map<EntityID, Point2D> passablePoint = new HashMap<>();
        area.getEdges().stream().filter(Edge::isPassable).forEach(edge -> {
            List<Edge> edges = new ArrayList<>(((Area) world.getEntity(edge.getNeighbour())).getEdges());
            edges.remove(edge);
            neighbourEdges.add(edges);
            passablePoint.put(edge.getNeighbour(), PositionUtil.getEdgePoint(edge));
        });
        this.neighbourEdgesMap.put(roadID, neighbourEdges);
        this.passablePointMap.put(roadID, passablePoint);
    }


}
