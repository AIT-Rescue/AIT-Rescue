package adk.sample.basic.tactics;

import adk.team.action.ActionClear;
import adk.team.tactics.TacticsPolice;
import adk.team.util.graph.PositionUtil;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.awt.geom.Line2D;
import java.util.*;

public class ClearPlanner {
    private StandardWorldModel world;

    public Map<EntityID, List<List<Edge>>> neighbourEdgesMap;
    public Map<EntityID, List<Point2D>> passablePointMap;

    public ClearPlanner(StandardWorldModel standardWorldModel) {
        this.world = standardWorldModel;
        this.neighbourEdgesMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
    }

    public ActionClear getAction(TacticsPolice tactics, Point2D targetPos, Road road) {
        Vector2D vector = this.getVector(tactics, targetPos, road);
        return new ActionClear(tactics, (int) (tactics.getOwner().getX() + vector.getX()), (int) (tactics.getOwner().getY() + vector.getY()));
    }

    public Vector2D getVector(TacticsPolice tactics, Point2D targetPos, Road road) {
        return this.getVector(new Point2D(tactics.getOwner().getX(), tactics.getOwner().getY()), targetPos, road);
    }

    public Vector2D getVector(Point2D agentPos, Point2D targetPos, Road road) {
        EntityID roadID = road.getID();
        this.analysisRoad(road);

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
        if (this.passablePointMap.containsKey(roadID)) {
            return;
        }
        List<List<Edge>> neighbourEdges = new ArrayList<>();
        List<Point2D> passablePoint = new ArrayList<>();
        road.getEdges().stream().filter(Edge::isPassable).forEach(edge -> {
            List<Edge> edges = new ArrayList<>(((Area) world.getEntity(edge.getNeighbour())).getEdges());
            edges.remove(edge);
            neighbourEdges.add(edges);
            passablePoint.add(PositionUtil.getEdgePoint(edge));
        });
        this.neighbourEdgesMap.put(roadID, neighbourEdges);
        this.passablePointMap.put(roadID, passablePoint);
    }

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
        return Line2D.linesIntersect(point.getX(), point.getY(), targetPoint.getX(), targetPoint.getY(), startX, startY, endX, endY) &&
                !PositionUtil.equalsPoint(targetPoint, ((startX + endX) / 2.0D), (startY + endX) / 2.0D, 1000.0D);
    }
}
