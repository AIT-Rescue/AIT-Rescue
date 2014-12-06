package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionClear;
import adk.team.action.ActionMove;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsPolice;
import adk.team.util.DebrisRemovalSelector;
import adk.team.util.PositionUtil;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.DebrisRemovalSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import com.google.common.collect.Lists;
import comlib.manager.MessageManager;
import comlib.message.information.BuildingMessage;
import comlib.message.information.CivilianMessage;
import comlib.message.information.PoliceForceMessage;
import rescuecore2.config.Config;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.awt.geom.Line2D;
import java.util.*;

public abstract class BasicPolice extends TacticsPolice implements RouteSearcherProvider, DebrisRemovalSelectorProvider {

    public DebrisRemovalSelector debrisRemovalSelector;

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
        this.debrisRemovalSelector = this.initDebrisRemovalSelector();
        this.beforeMove = false;
        this.neighbourEdgesMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
        this.clearListMap = new HashMap<>();
        this.beforeMove = false;
        this.count = 0;
    }

    public abstract DebrisRemovalSelector initDebrisRemovalSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public DebrisRemovalSelector getDebrisRemovalSelector() {
        return this.debrisRemovalSelector;
    }

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.agentPoint = new Point2D(this.me().getX(), this.me().getY());
        this.organizeUpdateInfo(updateWorldData, manager);
        StandardEntity area = this.location();
        EntityID areaID = area.getID();
        return (area instanceof Road) ? this.thinkDebrisRemoval(currentTime, areaID, (Road)area) : this.thinkMoveRoad(currentTime, manager);
    }

    public Action thinkMoveRoad(int currentTime, MessageManager manager) {
        if(this.me().getBuriedness() > 0) {
            if(!this.beforeMove) {
                this.beforeMove = true;
                manager.addSendMessage(new PoliceForceMessage(this.me()));
            }
            return new ActionRest(this);
        }
        this.beforeMove = true;
        this.target = this.debrisRemovalSelector.getTarget(currentTime);
        List<EntityID> path = null;
        if(this.target != null) {
            path = this.getRouteSearcher().getPath(currentTime, this.getID(), this.target);
        }
        return new ActionMove(this, path != null ? path : this.getRouteSearcher().noTargetMove(currentTime));
    }

    public Action thinkDebrisRemoval(int currentTime, EntityID roadID, Road road) {
        if(this.target == null) {
            this.target = this.debrisRemovalSelector.getTarget(currentTime);
            if(this.target == null) {
                this.beforeMove = true;
                return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
            }
        }
        if(roadID.getValue() != this.target.getValue()) {
            if(this.passable(road)) {
                this.beforeMove = true;
                List<EntityID> path = this.getRouteSearcher().getPath(currentTime, this.getID(), this.target);
                return new ActionMove(this, path != null ? path : this.getRouteSearcher().noTargetMove(currentTime));
            }
            this.target = roadID;
        }
        if(this.mainTargetPoint == null) {
            this.count = this.getClearList(road).size();
        }
        if(this.count != 0) {
            this.count--;
            this.mainTargetPoint = this.getClearList(road).get(this.count);
            Vector2D vector = this.getVector(this.agentPoint, this.mainTargetPoint, road);
            this.beforeMove = false;
            return new ActionClear(this, (int) (this.me().getX() + vector.getX()), (int) (this.me().getY() + vector.getY()));
        }
        if(this.beforeMove) {
            if(this.equalsPoint(this.agentPoint, this.mainTargetPoint, 1.0D)) {
                this.removeTargetPoint(road, this.mainTargetPoint);
                this.mainTargetPoint = null;
                List<Point2D> clearPoint = this.clearListMap.get(roadID);
                this.beforeMove = true;
                if(!clearPoint.isEmpty()) {
                    this.mainTargetPoint = clearPoint.get(0);
                    return new ActionMove(this, Lists.newArrayList(roadID), (int) this.mainTargetPoint.getX(), (int) this.mainTargetPoint.getY());
                }
                else {
                    this.target = this.debrisRemovalSelector.getTarget(currentTime);
                    List<EntityID> path = null;
                    if(this.target != null) {
                        path = this.getRouteSearcher().getPath(currentTime, this.getID(), this.target);
                    }
                    return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
                }
            }
            else {
                Vector2D vector = this.getVector(this.agentPoint, this.mainTargetPoint, road);
                this.beforeMove = false;
                return new ActionClear(this, (int) (this.me().getX() + vector.getX()), (int) (this.me().getY() + vector.getY()));
            }
        }
        else {
            this.beforeMove = true;
            return new ActionMove(this, Lists.newArrayList(roadID), (int) this.mainTargetPoint.getX(), (int) this.mainTargetPoint.getY());
        }
    }

    public void organizeUpdateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.getWorld().getEntity(next);
            if(entity instanceof Blockade) {
                this.debrisRemovalSelector.add((Blockade) entity);
            }
            else if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
            else if(entity instanceof Building) {
                Building b = (Building)entity;
                if(b.isOnFire()) {
                    manager.addSendMessage(new BuildingMessage(b));
                }
            }
        }
    }

    public Vector2D getVector(Point2D agentPos, Point2D targetPos, Road road) {
        EntityID roadID = road.getID();
        if(!this.clearListMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        List<Edge> edges = road.getEdges();
        if(this.canStraightForward(agentPos, targetPos, roadID, edges)) {
            return targetPos.minus(agentPos).normalised().scale(1000000);
        }
        else {
            Point2D edgePoint;
            Point2D min = null;
            for (Edge edge : edges) {
                edgePoint = this.getEdgePoint(edge);
                if (this.canStraightForward(agentPos, edgePoint, roadID, edges)) {
                    min = min != null ? PositionUtil.compareDistance(agentPos, min, edgePoint).translate(0.0D, 0.0D) : edgePoint.translate(0.0D, 0.0D);
                }
            }
            return min == null ? targetPos.minus(agentPos).normalised().scale(1000000) : min.minus(agentPos).normalised().scale(1000000);
        }
    }

    public void analysisRoad(Road road) {
        EntityID roadID = road.getID();
        if(this.clearListMap.containsKey(roadID)) {
            return;
        }
        List<List<Edge>> neighbourEdges = new ArrayList<>();
        List<Point2D> passablePoint = new ArrayList<>();
        road.getEdges().stream().filter(Edge::isPassable).forEach(edge -> {
            List<Edge> edges = ((Area) this.getWorld().getEntity(edge.getNeighbour())).getEdges();
            edges.remove(edge);
            neighbourEdges.add(edges);
            passablePoint.add(this.getEdgePoint(edge));
        });
        List<Point2D> clearList;
        if(road.getBlockades().isEmpty()) {
            clearList = new ArrayList<>();
            this.debrisRemovalSelector.remove(road);
        }
        else {
            clearList = new ArrayList<>(passablePoint);
            this.debrisRemovalSelector.add(road);
        }
        this.neighbourEdgesMap.put(roadID, neighbourEdges);
        this.passablePointMap.put(roadID, passablePoint);
        this.clearListMap.put(roadID, clearList);
    }

    public List<Point2D> getClearList(Road road) {
        EntityID roadID = road.getID();
        if(!this.clearListMap.containsKey(roadID)) {
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
        if(clearList.isEmpty()) {
            this.debrisRemovalSelector.remove(road);
        }
    }

    public boolean canStraightForward(Point2D point, Point2D targetPoint, EntityID roadID, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(point, targetPoint, edge)) {
                return false;
            }
        }
        for(List<Edge> list : this.neighbourEdgesMap.get(roadID)) {
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
        return Line2D.linesIntersect(point.getX(), point.getY(), targetPoint.getX(), targetPoint.getY(), startX, startY, endX, endY) && !this.equalsPoint(targetPoint, ((startX + endX) / 2.0D), (startY + endX) / 2.0D, 1.0D);
    }

    public Point2D getEdgePoint(Edge edge) {
        Point2D start = edge.getStart();
        Point2D end = edge.getEnd();
        return new Point2D(((start.getX() + end.getX()) / 2.0D), ((start.getY() + end.getY()) / 2.0D));
    }

    public boolean equalsPoint(Point2D point, Point2D targetPoint, double range) {
        return this.equalsPoint(point.getX(), point.getY(), targetPoint.getX(), targetPoint.getY(), range);
    }

    public boolean equalsPoint(Point2D point, double targetX, double targetY, double range) {
        return this.equalsPoint(point.getX(), point.getY(), targetX, targetY, range);
    }

    public boolean equalsPoint(double pointX, double pointY, double targetX, double targetY, double range) {
        return Double.compare(pointX, targetX + range) <= 0 && Double.compare(pointX, targetX - range) >= 0 && Double.compare(pointY, targetY + range) <= 0 && Double.compare(pointY, targetY - range) >= 0;
    }
}
