package sample.tactics;

import adk.team.util.RouteSearcher;
import adk.team.util.VictimSelector;
import adk.team.util.provider.RouteSearcherProvider;
import adk.team.util.provider.VictimSelectorProvider;
import adk.team.tactics.TacticsAmbulance;
import adk.team.precompute.PrecomputeAmbulance;
import adk.team.util.graph.RouteManager;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.AmbulanceTeam;
import sample.util.SampleRouteSearcher;
import sample.util.SampleVictimSelector;

public class SamplePrecomputeAmbulance extends PrecomputeAmbulance implements RouteSearcherProvider, VictimSelectorProvider {

    public VictimSelector victimSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config, MessageManager messageManager) {
        this.victimSelector = new SampleVictimSelector(this);
        this.routeSearcher = new SampleRouteSearcher(this, new RouteManager(this.world));
    }

    @Override
    public VictimSelector getVictimSelector() {
        return this.victimSelector;
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
