package adk.sample.basic.tactics;

import adk.team.util.graph.PositionUtil;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class PointSelector {

    private StandardWorldModel world;

    private Map<EntityID, Collection<Edge>> neighbourEdgesMap;
    private Map<EntityID, Map<EntityID, Point2D>> passablePointMap;
    private Map<EntityID, List<Point2D>> clearListMap;

    public PointSelector(StandardWorldModel world) {
        this.world = world;
        this.neighbourEdgesMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
        this.clearListMap = new HashMap<>();
        this.init(world);
    }

    public Collection<Edge> getNeighbourEdges(EntityID areaID) {
        return this.neighbourEdgesMap.get(areaID);
    }

    public Map<EntityID, Point2D> getPointMap(EntityID areaID) {
        return this.passablePointMap.get(areaID);
    }

    public List<Point2D> getClearPoints(EntityID areaID) {
        List<Point2D> list = this.clearListMap.get(areaID);
        if(list == null) {
            list = new ArrayList<>();
            Area area = (Area)this.world.getEntity(areaID);
            for(Edge edge : area.getEdges()) {
                if(edge.isPassable()) {
                    list.add(PositionUtil.getEdgePoint(edge));
                }
            }
            this.clearListMap.put(areaID, list);
        }
        return list;
    }

    private void init(StandardWorldModel world) {
        for(StandardEntity entity : world.getEntitiesOfType(
                StandardEntityURN.BUILDING,
                StandardEntityURN.AMBULANCE_CENTRE,
                StandardEntityURN.FIRE_STATION,
                StandardEntityURN.POLICE_OFFICE,
                StandardEntityURN.REFUGE,
                StandardEntityURN.GAS_STATION,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.ROAD
        )) {
            this.analysisArea((Area)entity, world);
        }
    }

    public void analysisArea(Area area, StandardWorldModel world) {
        EntityID roadID = area.getID();
        Collection<Edge> neighbourEdges = new HashSet<>();
        Map<EntityID, Point2D> passablePoint = new HashMap<>();
        area.getEdges().stream().filter(Edge::isPassable).forEach(edge -> {
            List<Edge> edges = new ArrayList<>(((Area) world.getEntity(edge.getNeighbour())).getEdges());
            edges.remove(edge);
            neighbourEdges.addAll(edges);
            passablePoint.put(edge.getNeighbour(), PositionUtil.getEdgePoint(edge));
        });
        this.neighbourEdgesMap.put(roadID, neighbourEdges);
        this.passablePointMap.put(roadID, passablePoint);
    }


}
