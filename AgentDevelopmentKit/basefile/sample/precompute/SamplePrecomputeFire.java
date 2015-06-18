package sample.tactics;

import adk.team.precompute.PrecomputeFire;
import adk.team.util.BuildingSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import adk.team.util.graph.RouteManager;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.FireBrigade;
import sample.util.SampleBuildingSelector;
import sample.util.SampleRouteSearcher;

public class SamplePrecomputeFire extends PrecomputeFire implements RouteSearcherProvider, BuildingSelectorProvider {

    public BuildingSelector buildingSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config, MessageManager messageManager) {
        this.buildingSelector = new SampleBuildingSelector(this);
        this.routeSearcher = new SampleRouteSearcher(this, new RouteManager(this.world));
    }

    @Override
    public BuildingSelector getBuildingSelector() {
        return this.buildingSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public String getTacticsName() {
        return "Sample";
    }
}
