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
import java.util.stream.Collectors;

public class AStarRouteSearcher implements RouteSearcher {

    private WorldProvider provider;
    private RouteManager routeManager;

    public AStarRouteSearcher(WorldProvider worldProvider) {
        this.provider = worldProvider;
        this.routeManager = new RouteManager(worldProvider.getWorld());
    }

    @Override
    public List<EntityID> getPath(int time, EntityID from, EntityID to) {
        StandardWorldModel world = this.provider.getWorld();
        RouteGraph graph = this.routeManager.getPassableGraph();
        if(from.getValue() == to.getValue()) {
            return Lists.newArrayList(from);
        }
        if(!graph.createPositionNode(world, from) || !graph.createPositionNode(world, to)) {
            return null;
        }
        RouteNode node = graph.getNode(from);
        if(node.isSingleNode()) {
            return Lists.newArrayList(from);
        }
        RouteNode goal = graph.getNode(to);
        if(goal.isSingleNode()) {
            return null;
        }
        //init
        List<RouteNode> nodePath = Lists.newArrayList(node);//old list
        RouteNode old = node;
        EntityID oldID = old.getID();
        ///

        //List<RouteNode> neighbours = node.getNeighbours().stream().filter(id -> oldID.getValue() != id.getValue()).map(graph::getNode).collect(Collectors.toList());
        //neighbours.sort(new DistanceComparator(goal));
        //for()
        ///
        nodePath.add(goal);
        return graph.getPath(nodePath);
    }

    private boolean searchNext(RouteGraph graph, List<RouteNode> nodePath, RouteNode currentNode, RouteNode target) {
        RouteNode old = nodePath.get(nodePath.size() - 1);
        EntityID oldID = old.getID();
        Set<EntityID> set = new HashSet<>(currentNode.getNeighbours());
        set.remove(oldID);
        List<RouteNode> neighbours = set.stream().map(graph::getNode).collect(Collectors.toList());
        neighbours.sort(new DistanceComparator(graph, currentNode, target));
        for(int i = 0; i < neighbours.size(); i++) {
            RouteNode select = neighbours.get(i);
            if(nodePath.contains(select)) {
                return false;
            }

        }


        return false;
    }

    @Override
    public List<EntityID> noTargetMove(int time) {
        return null;
    }

}
