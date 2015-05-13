package adk.team.util.graph;

import rescuecore2.misc.Pair;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.Edge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;

import java.util.Collection;

public class PositionUtil {
    public static StandardEntity getNearTarget(StandardWorldModel world, StandardEntity user, StandardEntity first, StandardEntity second) {
        return getNearTarget(world, user.getLocation(world), first, second);
    }

    public static StandardEntity getNearTarget(StandardWorldModel world, Pair<Integer, Integer> user, StandardEntity first, StandardEntity second) {
        return (valueOfCompare(user, first.getLocation(world)) <= valueOfCompare(user, second.getLocation(world))) ? first : second;
    }

    public static <T extends StandardEntity> T getNearTarget(StandardWorldModel world, StandardEntity user, Collection<T> targets) {
        return getNearTarget(world, user.getLocation(world), targets);
    }

    @SuppressWarnings("unchecked")
    public static <T extends StandardEntity> T getNearTarget(StandardWorldModel world, Pair<Integer, Integer> user, Collection<T> targets) {
        StandardEntity result = null;
        for(StandardEntity target : targets) {
            result = (result != null) ? getNearTarget(world, user, result, target) : target;
        }
        return (T)result;
    }

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, StandardEntity position, StandardEntity first, StandardEntity second) {
        return compareDistance(position.getLocation(world), first.getLocation(world), second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
        return (valueOfCompare(position, first) <= valueOfCompare(position, second)) ? first : second;
    }

    public static Pair<Integer, Integer> getNearPosition(StandardWorldModel world, StandardEntity position, Collection<Pair<Integer, Integer>> targets) {
        return getNearPosition(position.getLocation(world), targets);
    }

    public static Pair<Integer, Integer> getNearPosition(Pair<Integer, Integer> position, Collection<Pair<Integer, Integer>> targets) {
        Pair<Integer, Integer> result = null;
        for(Pair<Integer, Integer> target : targets) {
            result = (result != null) ? compareDistance(position, result, target) : target;
        }
        return result;
    }

    public static Point2D compareDistance(Point2D position, Point2D first, Point2D second) {
        return (valueOfCompare(position, first) <= valueOfCompare(position, second)) ? first : second;
    }

    public static Point2D getNearPosition(Point2D position, Collection<Point2D> targets) {
        Point2D result = null;
        for(Point2D target : targets) {
            result = (result != null) ? compareDistance(position, result, target) : target;
        }
        return result;
    }

    public static Point2D getEdgePoint(Edge edge) {
        Point2D start = edge.getStart();
        Point2D end = edge.getEnd();
        return new Point2D(((start.getX() + end.getX()) / 2.0D), ((start.getY() + end.getY()) / 2.0D));
    }

    public static double getLinearDistance(RouteNode from, RouteNode to) {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.hypot(dx, dy);
    }

    public static double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = fromX - toX;
        double dy = fromY - toY;
        return Math.hypot(dx, dy);
    }

    public static double getDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        /*double dx = (double)(position.first() - another.first());
        double dy = (double)(position.second() - another.second());
        return Math.hypot(dx, dy);*/
        return getDistance(position.first(), position.second(), another.first(), another.second());
    }

    public static double getDistance(Edge from, Edge to) {
        return getDistance(getEdgePoint(from), getEdgePoint(to));
    }

    public static double getDistance(Pair<Integer, Integer> from, Edge to) {
        return getDistance(from, getEdgePoint(to));
    }

    public static double getDistance(Pair<Integer, Integer> from, Point2D to) {
        /*double dx = from.first() - to.getX();
        double dy = from.second() - to.getY();
        return Math.hypot(dx, dy);*/
        return getDistance(from.first(), from.second(), to.getX(), to.getY());
    }

    public static double getDistance(Point2D from, Point2D to) {
        /*double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.hypot(dx, dy);*/
        return getDistance(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public static long valueOfCompare(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        long dx = position.first() - another.first();
        long dy = position.second() - another.second();
        return dx*dx + dy*dy;
    }

    public static double valueOfCompare(Point2D position, Point2D another) {
        double dx = position.getX() - another.getX();
        double dy = position.getY() - another.getY();
        return dx*dx + dy*dy;
    }

    public static boolean equalsPoint(Pair<Integer, Integer> point, Pair<Integer, Integer> targetPoint, double range) {
        return equalsPoint(point.first(), point.second(), targetPoint.first(), targetPoint.second(), range);
    }

    public static boolean equalsPoint(Point2D point, Point2D targetPoint, double range) {
        return equalsPoint(point.getX(), point.getY(), targetPoint.getX(), targetPoint.getY(), range);
    }

    public static boolean equalsPoint(Point2D point, double targetX, double targetY, double range) {
        return equalsPoint(point.getX(), point.getY(), targetX, targetY, range);
    }

    public static boolean equalsPoint(double pointX, double pointY, double targetX, double targetY, double range) {
        return Double.compare(pointX, targetX + range) <= 0 && Double.compare(pointX, targetX - range) >= 0 && Double.compare(pointY, targetY + range) <= 0 && Double.compare(pointY, targetY - range) >= 0;
    }
}
