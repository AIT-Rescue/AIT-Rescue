package adk.team.util;

import rescuecore2.misc.Pair;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;

import java.util.Collection;

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

    /*
    public static Pair<Integer, Integer> getNearPosition(StandardWorldModel world, StandardEntity position, Pair<Integer, Integer>... targets) {

        return getNearPosition(position.getLocation(world), targets);
    }

    public static Pair<Integer, Integer> getNearPosition(Pair<Integer, Integer> position, Pair<Integer, Integer>... targets) {
        Pair<Integer, Integer> result = null;
        for(Pair<Integer, Integer> target : targets) {
            result = (result != null) ? compareDistance(position, result, target) : target;
        }
        return result;
    }
    */

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

    private static long valueOfCompare(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        long dx = position.first() - another.first();
        long dy = position.second() - another.second();
        return dx*dx + dy*dy;
    }

    public static StandardEntity getNearTarget(StandardWorldModel world, StandardEntity user, StandardEntity first, StandardEntity second) {
        return (valueOfCompare(user.getLocation(world), first.getLocation(world)) <= valueOfCompare(user.getLocation(world), second.getLocation(world))) ? first : second;
    }

    public static StandardEntity getNearTarget(StandardWorldModel world, Pair<Integer, Integer> user, StandardEntity first, StandardEntity second) {
        return (valueOfCompare(user, first.getLocation(world)) <= valueOfCompare(user, second.getLocation(world))) ? first : second;
    }

    /*
    public static StandardEntity getNearTarget(StandardWorldModel world, StandardEntity user, StandardEntity... targets) {
        return getNearTarget(world, user.getLocation(world), targets);
    }

    public static StandardEntity getNearTarget(StandardWorldModel world, Pair<Integer, Integer> user, StandardEntity... targets) {
        StandardEntity result = null;
        for(StandardEntity target : targets) {
            result = (result != null) ? getNearTarget(world, user, result, target) : target;
        }
        return result;
    }
    */

    public static StandardEntity getNearTarget(StandardWorldModel world, StandardEntity user, Collection<? extends StandardEntity> targets) {
        return getNearTarget(world, user.getLocation(world), targets);
    }

    public static StandardEntity getNearTarget(StandardWorldModel world, Pair<Integer, Integer> user, Collection<? extends StandardEntity> targets) {
        StandardEntity result = null;
        for(StandardEntity target : targets) {
            result = (result != null) ? getNearTarget(world, user, result, target) : target;
        }
        return result;
    }

    //Point2D向けのメソッドも用意するべき
    private static double valueOfCompare(Point2D position, Point2D another) {
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
}
