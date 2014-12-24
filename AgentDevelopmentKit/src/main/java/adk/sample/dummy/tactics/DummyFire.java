package adk.sample.dummy.tactics;

import adk.sample.astar.util.AStarRouteSearcher;
import adk.sample.basic.event.BasicBuildingEvent;
import adk.sample.basic.util.BasicBuildingSelector;
import adk.sample.basic.tactics.BasicFire;
import adk.team.util.BuildingSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.graph.RouteManager;
import comlib.manager.MessageManager;
import comlib.message.information.MessageCivilian;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

public class DummyFire extends BasicFire {

    @Override
    public String getTacticsName() {
        return "Dummy System";
    }

    @Override
    public BuildingSelector initBuildingSelector() {
        return new BasicBuildingSelector(this);
    }

    @Override
    public RouteSearcher initRouteSearcher() {
        return new AStarRouteSearcher(this, new RouteManager(this.getWorld()));
    }


    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new BasicBuildingEvent(this, this));
    }

    @Override
    public void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.getWorld().getEntity(next);
            if(entity instanceof Building) {
                this.buildingSelector.add((Building) entity);
            }
            else if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new MessageCivilian(civilian));
                }
            }
            /*
            else if(entity instanceof Blockade) {
                manager.addSendMessage(new RoadMessage((Blockade) entity));
            }
            */
        }
    }
}
