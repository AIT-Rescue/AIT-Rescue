package sample.tactics;

import adk.team.tactics.TacticsFire;
import rescuecore2.standard.entities.FireBrigade;

public abstract class PrecomputeFire extends TacticsFire {
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
