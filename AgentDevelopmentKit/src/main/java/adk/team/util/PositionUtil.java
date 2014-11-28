package adk.team.util;

import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;

public class PositionUtil {

    public static Pair<Integer, Integer> compareDistance(StandardEntity position, StandardEntity first, StandardEntity second, StandardWorldModel world) {
        return compareDistance(position.getLocation(world), first.getLocation(world), second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(StandardEntity position, Pair<Integer, Integer> first, StandardEntity second, StandardWorldModel world) {
        return compareDistance(position.getLocation(world), first, second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(StandardEntity position, StandardEntity first, Pair<Integer, Integer> second, StandardWorldModel world) {
        return compareDistance(position.getLocation(world), first.getLocation(world), second);
    }

    public static Pair<Integer, Integer> compareDistance(Pair<Integer, Integer> position, StandardEntity first, StandardEntity second, StandardWorldModel world) {
        return compareDistance(position, first.getLocation(world), second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> first, StandardEntity second, StandardWorldModel world) {
        return compareDistance(position, first, second.getLocation(world));
    }

    public static Pair<Integer, Integer> compareDistance(Pair<Integer, Integer> position, StandardEntity first, Pair<Integer, Integer> second, StandardWorldModel world) {
        return compareDistance(position, first.getLocation(world), second);
    }

    public static Pair<Integer, Integer> compareDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
        return (compareValue(position, first) <= compareValue(position, second)) ? first : second;
    }

    private static long compareValue(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        long dx = position.first() - another.first();
        long dy = position.second() - another.second();
        return dx*dx + dy*dy;
    }

    public static <U extends StandardEntity, F extends StandardEntity, S extends StandardEntity> Object getNearEntity(U user, F first, S second, StandardWorldModel world) {
        return (compareValue(user.getLocation(world), first.getLocation(world)) <= compareValue(user.getLocation(world), second.getLocation(world))) ? first : second;
    }

    public static <F extends StandardEntity, S extends StandardEntity> Object getNearEntity(Pair<Integer, Integer> user, F first, S second, StandardWorldModel world) {
        return (compareValue(user, first.getLocation(world)) <= compareValue(user, second.getLocation(world))) ? first : second;
    }
}
