package adk.team.util.graph;

import rescuecore2.misc.Pair;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Edge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionUtil {

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, StandardEntity position, StandardEntity first, StandardEntity second) {
        return compareDistance(position.getLocation(world), first.getLocation(world), second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, StandardEntity position, Pair<Integer, Integer> first, StandardEntity second) {
        return compareDistance(position.getLocation(world), first, second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, StandardEntity position, StandardEntity first, Pair<Integer, Integer> second) {
        return compareDistance(position.getLocation(world), first.getLocation(world), second);
    }

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, Pair<Integer, Integer> position, StandardEntity first, StandardEntity second) {
        return compareDistance(position, first.getLocation(world), second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, Pair<Integer, Integer> position, Pair<Integer, Integer> first, StandardEntity second) {
        return compareDistance(position, first, second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(StandardWorldModel world, Pair<Integer, Integer> position, StandardEntity first, Pair<Integer, Integer> second) {
        return compareDistance(position, first.getLocation(world), second);
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

    public static long valueOfCompare(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        long dx = position.first() - another.first();
        long dy = position.second() - another.second();
        return dx*dx + dy*dy;
    }

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

    public static double valueOfCompare(Point2D position, Point2D another) {
        double dx = position.getX() - another.getX();
        double dy = position.getY() - another.getY();
        return dx*dx + dy*dy;
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

    public static double pointDistance(Edge from, Edge to) {
        return pointDistance(getEdgePoint(from), getEdgePoint(to));
    }

    public static double pointDistance(Point2D from, Point2D to) {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.hypot(dx, dy);
    }

    public static Map<EntityID, Double> getDistanceMap(StandardWorldModel world, List<EntityID> path) {
        Map<EntityID, Double> result = new HashMap<>();
        result.put(path.get(0), 0.0D);
        int size = path.size() - 1;
        result.put(path.get(size), 0.0D);
        for(int i = 1; i < size; i++) {
            EntityID areaID = path.get(i);
            Area area = (Area)world.getEntity(areaID);
            double distance = PositionUtil.pointDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
            result.put(areaID, distance);
        }
        return result;
    }

    /*public static double getDistance(RouteNode from, RouteNode to) {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.hypot(dx, dy);
    }*/
}
