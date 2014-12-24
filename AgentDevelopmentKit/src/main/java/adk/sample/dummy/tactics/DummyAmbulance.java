package adk.sample.dummy.tactics;

import adk.sample.astar.util.AStarRouteSearcher;
import adk.sample.basic.event.BasicAmbulanceEvent;
import adk.sample.basic.event.BasicCivilianEvent;
import adk.sample.basic.event.BasicFireEvent;
import adk.sample.basic.event.BasicPoliceEvent;
import adk.sample.basic.util.BasicVictimSelector;
import adk.sample.basic.tactics.BasicAmbulance;
import adk.team.util.RouteSearcher;
import adk.team.util.VictimSelector;
import adk.team.util.graph.RouteManager;
import comlib.manager.MessageManager;
import comlib.message.information.MessageBuilding;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

public class DummyAmbulance extends BasicAmbulance {

    @Override
    public String getTacticsName() {
        return "Dummy System";
    }

    @Override
    public VictimSelector initVictimSelector() {
        return new BasicVictimSelector(this);
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        return new AStarRouteSearcher(this, new RouteManager(this.getWorld()));
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicCivilianEvent(this, this));
        manager.registerEvent(new BasicAmbulanceEvent(this, this));
        manager.registerEvent(new BasicFireEvent(this, this));
        manager.registerEvent(new BasicPoliceEvent(this, this));
    }

    @Override
    public void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.getWorld().getEntity(next);
            if(entity instanceof Civilian) {
                this.victimSelector.add((Civilian) entity);
            }
            else if(entity instanceof Human) {
                this.victimSelector.add((Human)entity);
            }
            else if(entity instanceof Building) {
                Building b = (Building)entity;
                if(b.isOnFire()) {
                    manager.addSendMessage(new MessageBuilding(b));
                }
            }
            /*
            else if(entity instanceof Blockade) {
                //manager.addSendMessage(new RoadMessage(((Blockade)entity)));
            }
            */
        }
    }
}