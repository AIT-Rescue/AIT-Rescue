package sample;

import comlib.adk.team.tactics.basic.BasicPolice;
import comlib.adk.team.tactics.basic.event.BasicRoadEvent;
import comlib.adk.util.route.RouteSearcher;
import comlib.adk.util.route.sample.SampleRouteSearcher;
import comlib.adk.util.target.BlockadeSelector;
import comlib.adk.util.target.sample.SampleBlockadeSelector;
import comlib.manager.MessageManager;

public class SamplePolice extends BasicPolice {

    @Override
    public BlockadeSelector getBlockadeSelector() {
        if(this.blockadeSelector == null) {
            this.blockadeSelector = new SampleBlockadeSelector(this);
        }
        return this.blockadeSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        if(this.routeSearcher == null) {
            this.routeSearcher = new SampleRouteSearcher(this);
        }
        return this.routeSearcher;
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicRoadEvent(this));
    }
}
