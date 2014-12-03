package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionClear;
import adk.team.action.ActionMove;
import adk.team.tactics.TacticsPolice;
import adk.team.util.DebrisRemovalSelector;
import adk.team.util.PositionUtil;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.DebrisRemovalSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import comlib.message.information.BuildingMessage;
import comlib.message.information.CivilianMessage;
import rescuecore2.config.Config;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public abstract class BasicPolice extends TacticsPolice implements RouteSearcherProvider, DebrisRemovalSelectorProvider {

    public DebrisRemovalSelector debrisRemovalSelector;

    public RouteSearcher routeSearcher;

    public Map<EntityID, List<Edge>> passableEdgeMap;
    public Map<EntityID, List<Point2D>> passablePointMap;
    public Map<EntityID, List<Point2D>> allEdgePointMap;
    public Map<EntityID, List<Point2D>> clearTargetPointMap;
    public Point2D mainTargetPosition;
    public Point2D agentPosition;
    public boolean beforeMove;
    public int count;

    @Override
	public void preparation(Config config) {
		this.routeSearcher = this.initRouteSearcher();
        this.debrisRemovalSelector = this.initDebrisRemovalSelector();
        //
        this.passableEdgeMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
        this.allEdgePointMap = new HashMap<>();
        this.clearTargetPointMap = new HashMap<>();
        this.mainTargetPosition = null;
        this.agentPosition = null;
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
        this.agentPosition = new Point2D(this.me().getX(), this.me().getY());
        this.organizingUpdateInfo(currentTime, updateWorldData, manager);
        EntityID roadID = this.me().getPosition();
        Road road = (Road)this.getWorld().getEntity(roadID);
        //目標がない場合取得し，それでもNullならnoTargetWalk
        if(this.target == null) {
            this.target = this.debrisRemovalSelector.getTarget(currentTime);
            if(this.target == null) {
                this.beforeMove = true;
                return new ActionMove(this, this.routeSearcher.noTargetWalk(currentTime));
            }
        }
        //今いる場所がTarget地点と違う場合，通れるなら移動を行う．通れないと判定された場合，対象を今の場所に変更を行う
        if(!roadID.equals(this.target)) {
            if(this.passable(road)) {
                this.beforeMove = true;
                return new ActionMove(this, this.getRouteSearcher().getPath(currentTime, this.getID(), this.target));
            }
            this.target = roadID;
        }
        //対象地点まで移動済み．対象点の割り出しや，TargetPointを選択してない場合，解析・選択を行う
        if(this.mainTargetPosition == null) {
            List<Point2D> clearTargetPoint = this.getClearTargetPoint(road);
            this.count = clearTargetPoint.size();
        }
        //初期状態．とりあえず対象点全部へClear
        if(this.count != 0) {
            this.count--;
            this.mainTargetPosition = this.getClearTargetPoint(road).get(this.count);
            Vector2D vector = this.getVector(this.agentPosition, this.mainTargetPosition, road);
            this.beforeMove = false;
            return new ActionClear(this, (int) (this.me.getX() + vector.getX()), (int) (this.me.getY() + vector.getY()));
        }
        if(this.beforeMove) {
            if(this.mainTargetPosition.equals(this.agentPosition)) {
                this.removeTargetPoint(roadID, this.mainTargetPosition);
                List<Point2D> clearPoint = this.getClearTargetPoint(road);
                this.beforeMove = true;
                if(!clearPoint.isEmpty()) {
                    this.mainTargetPosition = clearPoint.get(0);
                    List<EntityID> path = new ArrayList<>(1);
                    path.add(roadID);
                    return new ActionMove(this, path, (int) this.mainTargetPosition.getX(), (int) this.mainTargetPosition.getY());
                }
                else {
                    this.mainTargetPosition = null;
                    this.target = this.debrisRemovalSelector.getTarget(currentTime);
                    List<EntityID> path = this.target != null ? this.getRouteSearcher().getPath(currentTime, this.getID(), this.target) : this.getRouteSearcher().noTargetWalk(currentTime);
                    return new ActionMove(this, path);
                }
            }
            else {
                Vector2D vector = this.getVector(this.agentPosition, this.mainTargetPosition, road);
                this.beforeMove = false;
                return new ActionClear(this, (int) (this.me().getX() + vector.getX()), (int) (this.me().getY() + vector.getY()));
            }
        }
        else {
            List<EntityID> path = new ArrayList<>(1);
            path.add(roadID);
            return new ActionMove(this, path, (int) this.mainTargetPosition.getX(), (int) this.mainTargetPosition.getY());
        }
    }



    public void organizingUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.model.getEntity(next);
            if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
            else if(entity instanceof Blockade) {
                this.debrisRemovalSelector.add((Blockade) entity);
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

    public boolean passable(Road road) {
        EntityID roadID = road.getID();
        if(!this.passableEdgeMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        return this.clearTargetPointMap.get(roadID).isEmpty();
    }


    public void analysisRoad(Road road) {
        EntityID roadID = road.getID();
        if(this.passableEdgeMap.containsKey(roadID)) {
            return;
        }
        List<Edge> edges = road.getEdges();
        List<Edge> passableEdge = new ArrayList<>();
        List<Point2D> passablePoint = new ArrayList<>();
        List<Point2D> allEdgePoint = new ArrayList<>();
        for(Edge edge : edges) {
            Point2D point = new Point2D(((edge.getStartX() + edge.getEndX()) / 2), ((edge.getStartY() + edge.getEndY()) / 2));
            allEdgePoint.add(point);
            if(edge.isPassable()) {
                passableEdge.add(edge);
                passablePoint.add(point);
            }
        }
        List<Point2D> clearTargetPoint;
        if(road.getBlockades().isEmpty()) {
            clearTargetPoint = new ArrayList<>();
            this.debrisRemovalSelector.remove(road);
        }
        else {
            clearTargetPoint = new ArrayList<>(passablePoint);
            this.debrisRemovalSelector.add(road);
        }
        this.passableEdgeMap.put(roadID, passableEdge);
        this.passablePointMap.put(roadID, passablePoint);
        this.allEdgePointMap.put(roadID, allEdgePoint);
        this.clearTargetPointMap.put(roadID, clearTargetPoint);
    }

    public List<Point2D> getClearTargetPoint(Road road) {
        EntityID roadID = road.getID();
        if(!this.passableEdgeMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        return this.clearTargetPointMap.get(roadID);
    }

    public void removeTargetPoint(Road road, Point2D point) {
        this.removeTargetPoint(road.getID(), point);
    }

    public void removeTargetPoint(EntityID roadID, Point2D point) {
        this.clearTargetPointMap.get(roadID).remove(point);
        if(this.clearTargetPointMap.get(roadID).isEmpty()) {
            this.debrisRemovalSelector.remove(roadID);
        }
    }

    public boolean canStraightForward(Point2D position, Point2D targetPosition, Road road) {
        return this.canStraightForward(position, targetPosition, road.getID(), road.getEdges());
    }
    public boolean canStraightForward(Point2D position, Point2D targetPosition, EntityID roadID, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(position, targetPosition, edge)) {
                return false;
            }
        }
        for(Edge edge : this.passableEdgeMap.get(roadID)) {
            if (!this.canStraightForward(position, targetPosition, ((Road) this.getWorld().getEntity(edge.getNeighbour())).getEdges())) {
                return false;
            }
        }
        return true;
    }
    public boolean canStraightForward(Point2D position, Point2D targetPosition, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(position, targetPosition, edge)) {
                return false;
            }
        }
        return true;
    }

    public boolean linesIntersect(Point2D position, Point2D targetPosition, Edge edge) {
        Point2D start = edge.getStart();
        double startX = start.getX();
        double startY = start.getY();
        Point2D end = edge.getEnd();
        double endX = end.getX();
        double endY = end.getY();
        return java.awt.geom.Line2D.linesIntersect(position.getX(), position.getY(), targetPosition.getX(), targetPosition.getY(), startX, startY, endX, endY) && !this.equalsPoint(targetPosition, ((startX + endX) / 2.0D), (startY + endX) / 2.0D);
    }

    public Point2D getEdgePoint(Edge edge) {
        Point2D start = edge.getStart();
        Point2D end = edge.getEnd();
        return new Point2D(((start.getX() + end.getX()) / 2.0D), ((start.getY() + end.getY()) / 2.0D));
    }

    public boolean equalsPoint(Point2D point, double x, double y) {
        return ((point.getX() == x) && (point.getY() == y));
    }
}
