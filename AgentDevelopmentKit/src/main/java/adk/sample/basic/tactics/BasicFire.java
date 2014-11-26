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

		public ChangeSet changed; // temp add

    @Override
    public void preparation(Config config) {
        this.buildingSelector = this.initBuildingSelector();
        this.routeSearcher = this.initRouteSearcher();
    }

    public abstract BuildingSelector initBuildingSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public Action think(int time, ChangeSet changed, MessageManager manager) {

			this.changed = changed; //temp add

        this.updateInfo(changed, manager);
        if (this.me.getWater() == 0) {
            this.target = null;
            return this.moveRefuge(time);
        }
        if(this.target != null) {
            Building building = (Building)this.model.getEntity(this.target);
            if(building.isOnFire()) {
                if(this.model.getDistance(this.agentID, this.target) <= this.maxDistance) {
                    return new ActionExtinguish(this, time, this.target, this.maxPower);
                }
                else {
                    List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return new ActionMove(this, time, path);
                    }
                    return new ActionMove(this, time, this.routeSearcher.noTargetWalk(time));
                }
            }
            else {
                this.buildingSelector.remove((Building)this.model.getEntity(this.target));
                this.target = this.buildingSelector.getTarget(time);
                if(this.target != null) {
                    List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return new ActionMove(this, time, path);
                    }
                }
                return new ActionMove(this, time, this.routeSearcher.noTargetWalk(time));
            }
        }
        else {
            //if(this.me.isWaterDefined()) { //??????????????????????????????????????????????????????
            if(this.location instanceof Refuge) {
                if (this.me.getWater() < this.maxWater) {
                    return new ActionRest(this, time);
                }
                else {
                    this.target = this.buildingSelector.getTarget(time);
                    if(this.target != null) {
                        List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                        if(path != null) {
                            path.remove(path.size() - 1);
                            return new ActionMove(this, time, path);
                        }
                    }
                    return new ActionMove(this, time, this.routeSearcher.noTargetWalk(time));
                }
            }
            else {
                this.target = this.buildingSelector.getTarget(time);
                if(this.target != null) {
                    List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return new ActionMove(this, time, path);
                    }
                }
                return new ActionMove(this, time, this.routeSearcher.noTargetWalk(time));
            }
            //}
        }
    }


    private void updateInfo(ChangeSet changed, MessageManager manager) {
        for (EntityID next : changed.getChangedEntities()) {
            StandardEntity entity = model.getEntity(next);
            if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
            else if(entity instanceof Blockade) {
                manager.addSendMessage(new RoadMessage((Blockade) entity));
            }
            else if(entity instanceof Building) {
                this.buildingSelector.add((Building) entity);
            }
        }
    }

    private Action moveRefuge(int time) {
        Refuge result = null;
        int minDistance = Integer.MAX_VALUE;
        for (Refuge refuge : this.refugeList) {
            int d = this.model.getDistance(this.me, refuge);
            if (minDistance >= d) {
                minDistance = d;
                result = refuge;
            }
        }
        List<EntityID> path = this.routeSearcher.getPath(time, this.me, result);
        return path != null ? new ActionMove(this, time, path) : new ActionMove(this, time, this.routeSearcher.noTargetWalk(time));
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
