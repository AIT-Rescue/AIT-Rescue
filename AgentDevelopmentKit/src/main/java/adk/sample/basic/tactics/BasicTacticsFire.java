package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionExtinguish;
import adk.team.action.ActionMove;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsFire;
import adk.team.util.BuildingSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.graph.PositionUtil;
import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import comlib.message.information.MessageFireBrigade;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class BasicTacticsFire extends TacticsFire implements RouteSearcherProvider, BuildingSelectorProvider {

    public BuildingSelector buildingSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config) {
        this.buildingSelector = this.initBuildingSelector();
        this.routeSearcher = this.initRouteSearcher();
    }

    public abstract BuildingSelector initBuildingSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public BuildingSelector getBuildingSelector() {
        return this.buildingSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    public abstract void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager);

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        //情報の整理
        this.organizeUpdateInfo(currentTime, updateWorldData, manager);
        //状態の確認
        if(this.me.getBuriedness() > 0) {
            manager.addSendMessage(new MessageFireBrigade(this.me, MessageFireBrigade.ACTION_REST, this.agentID));
            for(StandardEntity entity : this.world.getObjectsInRange(this.me, this.maxDistance)) {
                if(entity instanceof Building) {
                    Building building = (Building)entity;
                    this.target = building.getID();
                    //if (building.isOnFire() && (this.world.getDistance(this.agentID, this.target) <= this.maxDistance)) {
                    if(building.isOnFire()) {
                        return new ActionExtinguish(this, this.target, this.maxPower);
                    }
                }
            }
            return new ActionRest(this);
        }
        int waterLimit = 2;
        if (this.me.getWater() <= ((this.maxWater / 10) * waterLimit)) {
            this.target = null;
            return this.moveRefuge(currentTime);
        }
        if(this.location instanceof Refuge && (this.me.getWater() < this.maxWater)) {
            this.target = null;
            return new ActionRest(this);
        }
        this.target = this.target == null ? this.buildingSelector.getNewTarget(currentTime) : this.buildingSelector.updateTarget(currentTime, this.target);
        if(this.target == null) {
            return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
        }
        do {
            Building building = (Building) this.world.getEntity(this.target);
            if (building.isOnFire()) {
                return this.world.getDistance(this.agentID, this.target) <= this.maxDistance ? new ActionExtinguish(this, this.target, this.maxPower) : this.moveTarget(currentTime);
            } else {
                this.buildingSelector.remove(this.target);
            }
            this.target = this.buildingSelector.getNewTarget(currentTime);
        }while(this.target != null);
        return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = PositionUtil.getNearTarget(this.world, this.me, this.getRefuges());
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, result);
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }

    public Action moveTarget(int currentTime) {
        if(this.target != null) {
            List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
            if(path != null) {
                path.remove(this.target);
                return new ActionMove(this, path);
            }
            this.target = null;
        }
        return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
    }
}
