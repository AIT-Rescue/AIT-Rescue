package adk.sample.astar.util;

import adk.team.util.RouteSearcher;
import adk.team.util.graph.RouteGraph;
import adk.team.util.graph.RouteManager;
import adk.team.util.graph.RouteNode;
import adk.team.util.provider.WorldProvider;
import com.google.common.collect.Lists;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.List;

public class AstarRouteSearcher implements RouteSearcher{

    private WorldProvider provider;
    private RouteManager routeManager;

    public AstarRouteSearcher(WorldProvider worldProvider) {
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
        RouteNode start = graph.getNode(from);
        if(start.isSingleNode()) {
            return Lists.newArrayList(from);
        }
        RouteNode goal = graph.getNode(to);
        if(goal.isSingleNode()) {
            return null;
        }
        List<RouteNode> nodePath = Lists.newArrayList(start);
        ///
        //コンパレータいるかも(直線距離の比較) distance(goal, start.getNeighbourss()) > sort
        ///
        nodePath.add(goal);
        return graph.getPath(nodePath);
    }

    @Override
    public List<EntityID> noTargetMove(int time) {
        return null;
    }

}
