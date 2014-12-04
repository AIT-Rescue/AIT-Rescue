package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsPolice;
import adk.team.util.DebrisRemovalSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.DebrisRemovalSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

public abstract class BasicPolice extends TacticsPolice implements RouteSearcherProvider, DebrisRemovalSelectorProvider {

    public DebrisRemovalSelector debrisRemovalSelector;

    public RouteSearcher routeSearcher;

    public Point2D agentPoint;

    @Override
    public void preparation(Config config) {
        this.routeSearcher = this.initRouteSearcher();
        this.debrisRemovalSelector = this.initDebrisRemovalSelector();
    }

    public abstract DebrisRemovalSelector initDebrisRemovalSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public DebrisRemovalSelector getDebrisRemovalSelector() {
        return this.debrisRemovalSelector;
    }

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.agentPoint = new Point2D(this.me().getX(), this.me().getY());
        this.organizeUpdateInfo(updateWorldData, manager);
        return new ActionRest(this);
    }

    public void organizeUpdateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.model.getEntity(next);
            if(entity instanceof Blockade) {
                this.debrisRemovalSelector.add((Blockade) entity);
            }
            /*
            else if(entity instanceof Civilian) {

                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    //manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
            else if(entity instanceof Building) {
                Building b = (Building)entity;
                if(b.isOnFire()) {
                    //manager.addSendMessage(new BuildingMessage(b));
                }
            }
            */
        }
    }
}
