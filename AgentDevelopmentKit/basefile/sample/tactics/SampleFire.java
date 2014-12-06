package sample.tactics;

import adk.team.action.Action;
import adk.team.action.ActionExtinguish;
import adk.team.action.ActionMove;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsFire;
import adk.team.util.BuildingSelector;
import adk.team.util.PositionUtil;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import comlib.message.information.CivilianMessage;
import comlib.message.information.FireBrigadeMessage;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;
import sample.event.SampleBuildingEvent;
import sample.util.SampleBuildingSelector;
import sample.util.SampleRouteSearcher;

import java.util.List;

public class SampleFire extends TacticsFire implements RouteSearcherProvider, BuildingSelectorProvider {

    public BuildingSelector buildingSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config) {
        this.buildingSelector = new SampleBuildingSelector(this);
        this.routeSearcher = new SampleRouteSearcher(this);
    }

    @Override
    public String getTacticsName() {
        return "sample";
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new SampleBuildingEvent(this, this));
    }

    @Override
    public BuildingSelector getBuildingSelector() {
        return this.buildingSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizeUpdateInfo(updateWorldData, manager);
        if(this.me().getBuriedness() > 0) {
            manager.addSendMessage(new FireBrigadeMessage(this.me()));
            this.target = this.buildingSelector.getTarget(currentTime);
            if(this.target != null) {
                Building building = (Building) this.getWorld().getEntity(this.target);
                if (building.isOnFire() && (this.getWorld().getDistance(this.getID(), this.target) <= this.maxDistance)) {
                    return new ActionExtinguish(this, this.target, this.maxPower);
                }
            }
            return new ActionRest(this);
        }
        if (this.me().getWater() == 0) {
            this.target = null;
            return this.moveRefuge(currentTime);
        }
        if(this.target != null) {
            Building building = (Building)this.getWorld().getEntity(this.target);
            if(building.isOnFire()) {
                return this.getWorld().getDistance(this.getID(), this.target) <= this.maxDistance ? new ActionExtinguish(this, this.target, this.maxPower) : this.moveTarget(currentTime);
            }
            else {
                this.buildingSelector.remove(this.target);
                this.target = this.buildingSelector.getTarget(currentTime);
                return this.moveTarget(currentTime);
            }
        }
        else {
            if(this.location instanceof Refuge && (this.me().getWater() < this.maxWater)) {
                return new ActionRest(this);
            }
            else {
                this.target = this.buildingSelector.getTarget(currentTime);
                return this.moveTarget(currentTime);
            }
        }
    }

    public void organizeUpdateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = this.getWorld().getEntity(next);
            if(entity instanceof Building) {
                this.buildingSelector.add((Building) entity);
            }
            else if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
        }
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = PositionUtil.getNearTarget(this.getWorld(), this.me(), this.getRefuges());
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, result);
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }

    public Action moveTarget(int currentTime) {
        if(this.target != null) {
            List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me(), this.target);
            if(path != null) {
                path.remove(this.target);
                return new ActionMove(this, path);
            }
            this.target = null;
        }
        return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
    }

}
