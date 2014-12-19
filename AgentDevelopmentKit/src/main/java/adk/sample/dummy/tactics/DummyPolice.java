package adk.sample.dummy.tactics;

import adk.sample.astar.util.AStarRouteSearcher;
import adk.sample.basic.tactics.BasicPolice;
import adk.sample.basic.event.BasicRoadEvent;
import adk.sample.basic.util.BasicImpassableSelector;
import adk.team.util.ImpassableSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.graph.RouteManager;
import comlib.manager.MessageManager;

public class DummyPolice extends BasicPolice {

    @Override
    public String getTacticsName() {
        return "Dummy System";
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicRoadEvent(this, this));
    }


    @Override
    public ImpassableSelector initDebrisRemovalSelector() {
        if(this.impassableSelector == null) {
            this.impassableSelector = new BasicImpassableSelector(this);
        }
        return this.impassableSelector;
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        if(this.routeSearcher == null) {
            this.routeSearcher = new AStarRouteSearcher(this, new RouteManager(this.getWorld()));
        }
        return this.routeSearcher;
    }
}
