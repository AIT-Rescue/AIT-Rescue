package adk.sample.newbasic.tactics;

import adk.team.action.*;
import adk.team.tactics.TacticsPolice;
import adk.team.util.ImpassableSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.graph.PositionUtil;
import adk.team.util.graph.RouteGraph;
import adk.team.util.graph.RouteManager;
import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import com.google.common.collect.Lists;
import comlib.manager.MessageManager;
import comlib.message.information.MessageBuilding;
import comlib.message.information.MessageCivilian;
import comlib.message.information.MessageFireBrigade;
import comlib.message.information.MessagePoliceForce;
import rescuecore2.config.Config;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.awt.geom.Line2D;
import java.util.*;

public abstract class NewBasicPolice extends TacticsPolice implements RouteSearcherProvider, ImpassableSelectorProvider {

    public ImpassableSelector impassableSelector;

    public RouteSearcher routeSearcher;

    public Point2D agentPoint;
    public boolean beforeMove;
    public Map<EntityID, List<List<Edge>>> neighbourEdgesMap;
    public Map<EntityID, List<Point2D>> passablePointMap;
    public Map<EntityID, List<Point2D>> clearListMap;
    public Point2D mainTargetPoint;
    public int count;

    @Override
    public void preparation(Config config) {
        this.routeSearcher = this.initRouteSearcher();
        this.impassableSelector = this.initDebrisRemovalSelector();
        this.beforeMove = false;
        this.neighbourEdgesMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
        this.clearListMap = new HashMap<>();
        this.beforeMove = false;
        this.count = 0;
    }

    public abstract ImpassableSelector initDebrisRemovalSelector();

    public abstract RouteSearcher initRouteSearcher();

    public abstract void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager);

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public ImpassableSelector getImpassableSelector() {
        return this.impassableSelector;
    }

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizeUpdateInfo(currentTime, updateWorldData, manager);
        this.agentPoint = new Point2D(this.me().getX(), this.me().getY());
        if(this.me().getBuriedness() > 0) {
            manager.addSendMessage(new MessagePoliceForce(this.me()));
            List<EntityID> roads = ((Area)this.location()).getNeighbours();
            if(this.count <= 0) {
                this.count = roads.size();
            }
            this.count--;
            EntityID id = roads.get(this.count);
            
            return new ActionRest(this);
        }
        return new ActionRest(this);
    }

    public Vector2D getVector(Point2D agentPos, Point2D targetPos, Road road) {
        EntityID roadID = road.getID();
        if (!this.clearListMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        List<Edge> edges = road.getEdges();
        if (this.canStraightForward(agentPos, targetPos, roadID, edges)) {
            return targetPos.minus(agentPos).normalised().scale(1000000);
        } else {
            Point2D edgePoint;
            Point2D min = null;
            for (Edge edge : edges) {
                edgePoint = PositionUtil.getEdgePoint(edge);
                if (this.canStraightForward(agentPos, edgePoint, roadID, edges)) {
                    min = min != null ? PositionUtil.compareDistance(agentPos, min, edgePoint).translate(0.0D, 0.0D) : edgePoint.translate(0.0D, 0.0D);
                }
            }
            return min == null ? targetPos.minus(agentPos).normalised().scale(1000000) : min.minus(agentPos).normalised().scale(1000000);
        }
    }

    public void analysisRoad(Road road) {
        EntityID roadID = road.getID();
        if (this.clearListMap.containsKey(roadID)) {
            return;
        }
        List<List<Edge>> neighbourEdges = new ArrayList<>();
        List<Point2D> passablePoint = new ArrayList<>();
        for (Edge edge : road.getEdges()) {
            if (edge.isPassable()) {
                List<Edge> edges = new ArrayList<>(((Area) this.getWorld().getEntity(edge.getNeighbour())).getEdges());
                edges.remove(edge);
                neighbourEdges.add(edges);
                passablePoint.add(PositionUtil.getEdgePoint(edge));
            }
        }
        List<Point2D> clearList;
        if (road.getBlockades().isEmpty()) {
            clearList = new ArrayList<>();
            this.impassableSelector.remove(road);
        } else {
            clearList = new ArrayList<>(passablePoint);
            this.impassableSelector.add(road);
        }
        this.neighbourEdgesMap.put(roadID, neighbourEdges);
        this.passablePointMap.put(roadID, passablePoint);
        this.clearListMap.put(roadID, clearList);
    }

    public List<Point2D> getClearList(Road road) {
        EntityID roadID = road.getID();
        if (!this.clearListMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        return this.clearListMap.get(roadID);
    }

    public boolean passable(Road road) {
        return this.getClearList(road).isEmpty();
    }

    public void removeTargetPoint(Road road, Point2D point) {
        EntityID roadID = road.getID();
        List<Point2D> clearList = this.clearListMap.get(roadID);
        clearList.remove(point);
        this.clearListMap.put(roadID, clearList);
        if (clearList.isEmpty()) {
            this.impassableSelector.remove(road);
        }
    }

    //move tool ???

    public boolean canStraightForward(Point2D point, Point2D targetPoint, EntityID roadID, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(point, targetPoint, edge)) {
                return false;
            }
        }
        for (List<Edge> list : this.neighbourEdgesMap.get(roadID)) {
            if (!this.canStraightForward(point, targetPoint, list)) {
                return false;
            }
        }
        return true;
    }

    public boolean canStraightForward(Point2D point, Point2D targetPoint, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(point, targetPoint, edge)) {
                return false;
            }
        }
        return true;
    }

    public boolean linesIntersect(Point2D point, Point2D targetPoint, Edge edge) {
        Point2D start = edge.getStart();
        double startX = start.getX();
        double startY = start.getY();
        Point2D end = edge.getEnd();
        double endX = end.getX();
        double endY = end.getY();
        return Line2D.linesIntersect(point.getX(), point.getY(), targetPoint.getX(), targetPoint.getY(), startX, startY, endX, endY) && !PositionUtil.equalsPoint(targetPoint, ((startX + endX) / 2.0D), (startY + endX) / 2.0D, 10.0D);
    }
}
