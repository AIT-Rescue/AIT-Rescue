package adk.sample.dummy.tactics;

import adk.sample.astar.util.AStarRouteSearcher;
import adk.sample.basic.tactics.BasicPolice;
import adk.sample.basic.event.BasicRoadEvent;
import adk.sample.basic.util.BasicDebrisRemovalSelector;
import adk.sample.basic.util.BasicRouteSearcher;
import adk.team.util.DebrisRemovalSelector;
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
    public DebrisRemovalSelector initDebrisRemovalSelector() {
        if(this.debrisRemovalSelector == null) {
            this.debrisRemovalSelector = new BasicDebrisRemovalSelector(this);
        }
        return this.debrisRemovalSelector;
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        if(this.routeSearcher == null) {
            this.routeSearcher = new AStarRouteSearcher(this, new RouteManager(this.getWorld()));
        }
        return this.routeSearcher;
    }
}
