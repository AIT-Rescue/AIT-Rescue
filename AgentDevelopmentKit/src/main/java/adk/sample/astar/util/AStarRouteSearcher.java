package adk.sample.astar.util;

import adk.team.util.RouteSearcher;
import adk.team.util.graph.RouteGraph;
import adk.team.util.graph.RouteManager;
import adk.team.util.graph.RouteNode;
import adk.team.util.provider.WorldProvider;
import com.google.common.collect.Lists;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class AStarRouteSearcher implements RouteSearcher {

    private WorldProvider provider;
    private RouteManager routeManager;

    public AStarRouteSearcher(WorldProvider worldProvider, RouteManager manager) {
        this.provider = worldProvider;
        this.routeManager = manager;
    }

    //https://code.google.com/p/a-star-java/source/browse/AStar/src/aStar/AStar.java?r=7
    @Override
    public List<EntityID> getPath(int time, EntityID startID, EntityID goalID) {
        StandardWorldModel world = this.provider.getWorld();
        RouteGraph graph = this.routeManager.getPassableGraph();
        // check
        if(startID.getValue() == goalID.getValue()) {
            return Lists.newArrayList(startID);
        }
        if(!graph.createPositionNode(world, startID) || !graph.createPositionNode(world, goalID)) {
            return null;
        }
        RouteNode start = graph.getNode(startID);
        if(start.isSingleNode()) {
            return Lists.newArrayList(startID);
        }
        RouteNode goal = graph.getNode(goalID);
        if(goal.isSingleNode()) {
            return null;
        }
        // init
        Set<EntityID> closed = new HashSet<>();
        List<RouteNode> open = Lists.newArrayList(start);
        Map<EntityID, EntityID> previousNodeMap = new HashMap<>();
        Map<EntityID, Double> distanceFromStart = new HashMap<>();
        Map<EntityID, Double> distanceToEnd = new HashMap<>();
        // process
        while(open.size() != 0) {
            RouteNode current = open.get(0); //sort
            EntityID currentID = current.getID();
            //目的地に着いた時
            if(currentID.getValue() == goalID.getValue()) {
                List<RouteNode> nodePath = Lists.newArrayList(current);
                EntityID id = currentID;
                while(previousNodeMap.containsKey(id)) {
                    id = previousNodeMap.get(id);
                    nodePath.add(graph.getNode(id));
                }
                Collections.reverse(nodePath);
                return graph.getPath(nodePath); // create path
            }
            // reset
            open.clear();
            closed.add(currentID);
            // search next
            for(EntityID neighbourID : current.getNeighbours()) {
                if (closed.contains(neighbourID)) {
                    continue;
                }
                RouteNode neighbour = graph.getNode(neighbourID);
                double currentDistance = distanceFromStart.containsKey(currentID) ? distanceFromStart.get(currentID) : 0.0D;
                double neighbourDistance = currentDistance + graph.getEdge(current, neighbour).getDistance();
                if(!open.contains(neighbour)) {
                    open.add(neighbour);
                    previousNodeMap.put(neighbourID, currentID);
                    distanceFromStart.put(neighbourID, neighbourDistance);
                }
            }
            open.sort(new DistanceComparator(goal, distanceFromStart, distanceToEnd));
        }
        return null;
    }

    @Override
    public List<EntityID> noTargetMove(int time) {
        return null;
    }

}
