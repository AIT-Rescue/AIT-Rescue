package sample.tactics;

import adk.team.tactics.TacticsPolice;
import rescuecore2.standard.entities.PoliceForce;

public class SamplePrecomputePolice extends PrecomputePolice implements RouteSearcherProvider, ImpassableSelectorProvider {

    public ImpassableSelector impassableSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config, MessageManager messageManager) {
        this.routeSearcher = new SampleRouteSearcher(this, new RouteManager(this.world));
        this.impassableSelector = new SampleImpassableSelector(this);
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public ImpassableSelector getImpassableSelector() {
        return this.impassableSelector;
    }

    @Override
    public String getTacticsName() {
        return "Sample";
    }
}
