package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionExtinguish;
import adk.team.action.ActionMove;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsFire;
import adk.team.util.BuildingSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import comlib.message.information.CivilianMessage;
import comlib.message.information.RoadMessage;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class BasicFire extends TacticsFire implements RouteSearcherProvider, BuildingSelectorProvider {

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
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizingUpdateInfo(updateWorldData, manager);
        if (this.me.getWater() == 0) {
            this.target = null;
            return this.moveRefuge(currentTime);
        }
        if(this.target != null) {
            Building building = (Building)this.model.getEntity(this.target);
            if(building.isOnFire()) {
                if(this.model.getDistance(this.agentID, this.target) <= this.maxDistance) {
                    return new ActionExtinguish(this, this.target, this.maxPower);
                }
                else {
                    List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return new ActionMove(this, path);
                    }
                    return new ActionMove(this, this.routeSearcher.noTargetWalk(currentTime));
                }
            }
            else {
                this.buildingSelector.remove((Building)this.model.getEntity(this.target));
                this.target = this.buildingSelector.getTarget(currentTime);
                if(this.target != null) {
                    List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return new ActionMove(this, path);
                    }
                }
                return new ActionMove(this, this.routeSearcher.noTargetWalk(currentTime));
            }
        }
        else {
            //if(this.me.isWaterDefined()) { //??????????????????????????????????????????????????????
            if(this.location instanceof Refuge) {
                if (this.me.getWater() < this.maxWater) {
                    return new ActionRest(this);
                }
                else {
                    this.target = this.buildingSelector.getTarget(currentTime);
                    if(this.target != null) {
                        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                        if(path != null) {
                            path.remove(path.size() - 1);
                            return new ActionMove(this, path);
                        }
                    }
                    return new ActionMove(this, this.routeSearcher.noTargetWalk(currentTime));
                }
            }
            else {
                this.target = this.buildingSelector.getTarget(currentTime);
                if(this.target != null) {
                    List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return new ActionMove(this, path);
                    }
                }
                return new ActionMove(this, this.routeSearcher.noTargetWalk(currentTime));
            }
            //}
        }
    }

    /*
    public Action getNoTargetAction(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        return new ActionRest(this, currentTime);
    }

    public Action getTargetAction(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        return new ActionRest(this, currentTime);
    }

    public EntityID selectTarget(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        return this.buildingSelector.getTarget(currentTime);
    }

    public boolean checkOwnState(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        return false;
    }
    */

    public void organizingUpdateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = model.getEntity(next);
            if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    //manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
            else if(entity instanceof Blockade) {
                //manager.addSendMessage(new RoadMessage((Blockade) entity));
            }
            else if(entity instanceof Building) {
                this.buildingSelector.add((Building) entity);
            }
        }
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = null;
        int minDistance = Integer.MAX_VALUE;
        for (Refuge refuge : this.refugeList) {
            int d = this.model.getDistance(this.me, refuge);
            if (minDistance >= d) {
                minDistance = d;
                result = refuge;
            }
        }
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, result);
        return path != null ? new ActionMove(this, path) : new ActionMove(this, this.routeSearcher.noTargetWalk(currentTime));
    }

    @Override
    public BuildingSelector getBuildingSelector() {
        return this.buildingSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }
}
