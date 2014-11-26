package adk.sample.dummy.tactics;

import adk.sample.basic.tactics.BasicPolice;
import adk.sample.basic.event.BasicRoadEvent;
import adk.sample.basic.util.BasicBlockadeSelector;
import adk.sample.basic.util.BasicRouteSearcher;
import adk.team.util.BlockadeSelector;
import adk.team.util.RouteSearcher;
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
    public BlockadeSelector initBlockadeSelector() {
        if(this.blockadeSelector == null) {
            this.blockadeSelector = new BasicBlockadeSelector(this);
        }
        return this.blockadeSelector;
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        if(this.routeSearcher == null) {
            this.routeSearcher = new BasicRouteSearcher(this);
        }
        return this.routeSearcher;
    }
}
