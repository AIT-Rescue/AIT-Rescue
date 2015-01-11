package adk.sample.dummy.tactics;

import adk.sample.basic.event.BasicAmbulanceEvent;
import adk.sample.basic.event.BasicCivilianEvent;
import adk.sample.basic.event.BasicFireEvent;
import adk.sample.basic.event.BasicPoliceEvent;
import adk.sample.basic.util.BasicRouteSearcher;
import adk.sample.basic.util.BasicVictimSelector;
import adk.sample.basic.tactics.BasicTacticsAmbulance;
import adk.team.util.RouteSearcher;
import adk.team.util.VictimSelector;
import comlib.manager.MessageManager;
import comlib.message.information.MessageBuilding;
import comlib.message.information.MessageRoad;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

public class DummyTacticsAmbulance extends BasicTacticsAmbulance {

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
        return new BasicRouteSearcher(this);
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
            else if(entity instanceof Blockade) {
                Blockade blockade = (Blockade) entity;
                manager.addSendMessage(new MessageRoad((Road)this.world.getEntity(blockade.getPosition()), blockade, false));
            }
        }
    }
}