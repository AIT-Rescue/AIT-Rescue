package sample;

import comlib.adk.team.tactics.basic.BasicAmbulance;
import comlib.adk.team.tactics.basic.event.BasicAmbulanceEvent;
import comlib.adk.team.tactics.basic.event.BasicCivilianEvent;
import comlib.adk.team.tactics.basic.event.BasicFireEvent;
import comlib.adk.team.tactics.basic.event.BasicPoliceEvent;
import comlib.adk.util.route.RouteSearcher;
import comlib.adk.util.route.sample.SampleRouteSearcher;
import comlib.adk.util.target.VictimSelector;
import comlib.adk.util.target.sample.SampleVictimSelector;
import comlib.manager.MessageManager;

public class SampleAmbulance extends BasicAmbulance {

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicCivilianEvent(this));
        manager.registerEvent(new BasicAmbulanceEvent(this));
        manager.registerEvent(new BasicFireEvent(this));
        manager.registerEvent(new BasicPoliceEvent(this));
    }

    @Override
    public VictimSelector getVictimSelector() {
        if(this.victimSelector == null) {
            this.victimSelector = new SampleVictimSelector(this);
        }
        return this.victimSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        if(this.routeSearcher == null) {
            this.routeSearcher = new SampleRouteSearcher(this);
        }
        return this.routeSearcher;
    }
}
