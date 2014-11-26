package adk.sample.dummy.tactics;

import adk.sample.basic.tactics.BasicFire;
import adk.sample.basic.event.BasicBuildingEvent;
import adk.sample.basic.util.BasicBuildingSelector;
import adk.sample.basic.util.BasicRouteSearcher;
import adk.team.util.BuildingSelector;
import adk.team.util.RouteSearcher;
import comlib.manager.MessageManager;

public class DummyFire extends BasicFire {

    @Override
    public String getTacticsName() {
        return "Dummy System";
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicBuildingEvent(this, this));
    }

    @Override
    public BuildingSelector initBuildingSelector() {
        return new BasicBuildingSelector(this);
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        return new BasicRouteSearcher(this);
    }
}
