package adk.team.util.refuge;

import adk.team.util.graph.PositionUtil;
import adk.team.util.provider.RouteSearcherProvider;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class ReservoirSelector {

    private StandardWorldModel world;
    private Config config;
    private RouteSearcherProvider provider;

    public double averageDistance;
    public boolean firstUpdate;
    public static final double DEFAULT_AVERAGE = 37615.0D;
    public static final int DEFAULT_COUNT = 3;
    public int count;

    public List<Reservoir> stations;

    public ReservoirSelector(StandardWorldModel standardWorldModel, Config c, RouteSearcherProvider routeSearcherProvider) {
        this(standardWorldModel, c, routeSearcherProvider, DEFAULT_COUNT);
    }

    public ReservoirSelector(StandardWorldModel standardWorldModel, Config c, RouteSearcherProvider routeSearcherProvider, int operationCount) {
        this.world = standardWorldModel;
        this.config = c;
        this.provider = routeSearcherProvider;
        this.averageDistance = DEFAULT_AVERAGE;
        this.firstUpdate = true;
        this.count = operationCount;
        this.initList();
    }

    private void initList() {
        this.stations = new ArrayList<>();
        for(StandardEntity entity : this.world.getEntitiesOfType(StandardEntityURN.REFUGE, StandardEntityURN.HYDRANT)) {
            this.stations.add(Reservoir.getInstance(entity, this.world, this.config));
        }
    }

    public void updateAverage(int time, Human human) {
        int[] array = human.getPositionHistory();
        if(array.length >= 4) {
            double distance = getMoveDistance(array);
            if (firstUpdate) {
                this.averageDistance = distance;
                this.firstUpdate = false;
            } else {
                this.averageDistance = ((this.averageDistance * (time - 1)) + distance) / time;
            }
        }
    }

    public void updateAverage(int time, double distance) {
        if(firstUpdate) {
            this.averageDistance = distance;
            firstUpdate = false;
        }
        else {
            this.averageDistance = ((this.averageDistance * (time - 1)) + distance) / time;
        }
    }

    public void resetAverage(double distance) {
        this.averageDistance = distance;
    }

    public ReservoirResult selectByWater(int time, Human human, int water) {
        return this.selectByWater(time, (Area)human.getPosition(this.world), water);
    }

    public ReservoirResult selectByWater(int time, Area position, int water) {
        if(this.stations.isEmpty()) {
            this.initList();
            if(this.stations.isEmpty()) {
                return null;
            }
        }
        this.stations.sort(new ReservoirComparator(position.getLocation(this.world)));
        if(position.getID().getValue() == this.stations.get(0).getID().getValue()) {
            Reservoir reservoir = this.stations.get(0);
            List<EntityID> path = new ArrayList<>();
            path.add(position.getID());
            return new ReservoirResult(reservoir, water, (int)Math.ceil((double)water / (double)reservoir.getSupply()), path);
        }
        ReservoirResult best = null;
        List<EntityID> path;
        int step = Integer.MAX_VALUE;
        int c = this.count;
        for(int i = 0; i < c; i++) {
            if(i == this.stations.size()) {
                return best;
            }
            Reservoir station = this.stations.get(i);
            path = this.provider.getRouteSearcher().getPath(time, position.getID(), station.getArea());
            if(path == null || path.isEmpty()) {
                c++;
                continue;
            }
            int s = (int)Math.ceil(getPathDistance(this.world, path) / this.averageDistance);
            s += (int)Math.ceil((double)water / (double)station.getSupply());
            if(step > s) {
                best = new ReservoirResult(station, water, s, path);
                step = s;
            }
        }
        return best;
    }

    public ReservoirResult selectByStep(int time, Human human, int step) {
        return this.selectByStep(time, (Area)human.getPosition(this.world), step);
    }

    public ReservoirResult selectByStep(int time, Area position, int step) {
        if(this.stations.isEmpty()) {
            this.initList();
            if(this.stations.isEmpty()) {
                return null;
            }
        }
        this.stations.sort(new ReservoirComparator(position.getLocation(this.world)));
        if(position.getID().getValue() == this.stations.get(0).getID().getValue()) {
            Reservoir reservoir = this.stations.get(0);
            List<EntityID> path = new ArrayList<>();
            path.add(position.getID());
            return new ReservoirResult(reservoir, step * reservoir.getSupply(), step, path);
        }
        ReservoirResult best = null;
        List<EntityID> path;
        int water = -1;
        int c = this.count;
        for(int i = 0; i < c; i++) {
            if(i == this.stations.size()) {
                return best;
            }
            Reservoir station = this.stations.get(i);
            path = this.provider.getRouteSearcher().getPath(time, position.getID(), station.getArea());
            if(path == null || path.isEmpty()) {
                c++;
                continue;
            }
            int s = (int)Math.ceil(getPathDistance(this.world, path) / this.averageDistance);
            if(step < s) {
                continue;
            }
            int w = (step - s) * station.getSupply();
            if(water < w) {
                best = new ReservoirResult(station, w, step, path);
                water = w;
            }
        }
        return best;
    }

    //move PositionUtil
    public static double getPathDistance(StandardWorldModel world, List<EntityID> path) {
        if(path.size() < 2){
            return 0.0D;
        }
        double distance = 0.0D;
        int limit = path.size() - 1;

        Area area = (Area)world.getEntity(path.get(0));
        distance += PositionUtil.getDistance(area.getLocation(world), area.getEdgeTo(path.get(1)));
        area = (Area)world.getEntity(path.get(limit));
        distance += PositionUtil.getDistance(area.getLocation(world), area.getEdgeTo(path.get(limit - 1)));

        EntityID areaID;
        for(int i = 1; i < limit; i++) {
            areaID = path.get(i);
            area = (Area)world.getEntity(areaID);
            distance += PositionUtil.getDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
        }
        return distance;
    }

    public static double getMoveDistance(int[] array) {
        double result = 0.0D;
        int limit = array.length - 2;
        for(int i = 0; i < limit; i+=2) {
            result += getDistance(array[i], array[i + 1], array[i + 2], array[i + 3]);
        }
        return result;
    }

    public static double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = fromX - toX;
        double dy = fromY - toY;
        return Math.hypot(dx, dy);
    }

}
