package adk.sample.dummy.tactics;

import adk.sample.basic.event.BasicRoadEvent;
import adk.sample.basic.util.BasicImpassableSelector;
import adk.sample.basic.tactics.BasicTacticsPolice;
import adk.sample.basic.util.BasicRouteSearcher;
import adk.team.util.ImpassableSelector;
import adk.team.util.RouteSearcher;
import comlib.manager.MessageManager;
import comlib.message.information.MessageBuilding;
import comlib.message.information.MessageCivilian;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

public class DummyTacticsPolice extends BasicTacticsPolice {

    @Override
    public String getTacticsName() {
        return "Dummy System";
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicRoadEvent(this, this));
    }


    @Override
    public ImpassableSelector initImpassableSelector() {
        return new BasicImpassableSelector(this);
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        return new BasicRouteSearcher(this);
    }

    @Override
    public void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.getWorld().getEntity(next);
            if(entity instanceof Blockade) {
                this.impassableSelector.add((Blockade) entity);
            }
            else if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new MessageCivilian(civilian));
                }
            }
            else if(entity instanceof Building) {
                Building b = (Building)entity;
                if(b.isOnFire()) {
                    manager.addSendMessage(new MessageBuilding(b));
                }
            }
        }
    }
}
