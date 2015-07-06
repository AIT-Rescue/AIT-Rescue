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

    private PointSelector points;

    public ClearPlanner(StandardWorldModel standardWorldModel, PointSelector pointSelector) {
        this.world = standardWorldModel;
        this.points = pointSelector;
    }

    public ActionClear getAction(TacticsPolice tactics, EntityID next) {
        return this.getAction(tactics, (Area)this.world.getEntity(next));
    }

    public ActionClear getAction(TacticsPolice tactics, Area next) {
        Vector2D vector = this.getVector(tactics, next);
        if(vector == null) {
            return null;
        }
        return new ActionClear(
                tactics,
                (int) (tactics.getOwner().getX() + vector.getX()),
                (int) (tactics.getOwner().getY() + vector.getY())
        );
    }

    public ActionClear getAction(TacticsPolice tactics, Point2D targetPos) {
        Vector2D vector = this.getVector(new Point2D(tactics.getOwner().getX(), tactics.getOwner().getY()), (Area)tactics.location, targetPos);
        if(vector == null) {
            return null;
        }
        return new ActionClear(
                tactics,
                (int) (tactics.getOwner().getX() + vector.getX()),
                (int) (tactics.getOwner().getY() + vector.getY())
        );
    }

    public Vector2D getVector(TacticsPolice tactics, Area next) {
        return this.getVector(new Point2D(tactics.getOwner().getX(), tactics.getOwner().getY()), (Area)tactics.location, next);
    }

    public Vector2D getVector(Point2D agentPos, Area location, Area next) {
        Point2D nextPoint = this.points.getPointMap(location.getID()).get(next.getID());
        return this.getVector(agentPos, location, nextPoint);
    }

    public Vector2D getVector(Point2D agentPos, Area location, Point2D targetPos) {
        EntityID areaID = location.getID();
        List<Edge> edges = location.getEdges();
        if (this.canStraightForward(agentPos, targetPos, areaID, edges)) {
            return targetPos.minus(agentPos).normalised().scale(1000000);
        } else {
            Point2D edgePoint;
            Point2D min = null;
            for (Edge edge : edges) {
                edgePoint = PositionUtil.getEdgePoint(edge);
                if (this.canStraightForward(agentPos, edgePoint, areaID, edges)) {
                    min = min != null ? PositionUtil.compareDistance(agentPos, min, edgePoint).translate(0.0D, 0.0D) : edgePoint.translate(0.0D, 0.0D);
                }
            }
            return min == null ? targetPos.minus(agentPos).normalised().scale(1000000) : min.minus(agentPos).normalised().scale(1000000);
        }
    }

    public boolean canStraightForward(Point2D point, Point2D targetPoint, EntityID roadID, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(point, targetPoint, edge)) {
                return false;
            }
        }
        return this.canStraightForward(point, targetPoint, this.points.getNeighbourEdges(roadID));
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
                !PositionUtil.equalsPoint(targetPoint, ((startX + endX) / 2.0D), (startY + endY) / 2.0D, 1000.0D);
    }
}
