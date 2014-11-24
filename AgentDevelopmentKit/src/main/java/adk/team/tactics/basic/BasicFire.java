package comlib.adk.team.tactics.basic;

import comlib.adk.team.tactics.FireBrigadeTactics;
import comlib.adk.util.action.AmbulanceAction;
import comlib.adk.util.action.FireAction;
import comlib.adk.util.route.RouteSearcher;
import comlib.adk.util.route.RouteUtil;
import comlib.adk.util.target.BuildingSelector;
import comlib.manager.MessageManager;
import comlib.message.information.CivilianMessage;
import comlib.message.information.RoadMessage;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class BasicFire extends FireBrigadeTactics {

    public BuildingSelector buildingSelector;

    public RouteSearcher routeSearcher;

		public ChangeSet changed; // temp add

    @Override
    public void preparation() {
        this.model.indexClass(
                StandardEntityURN.BUILDING,
                StandardEntityURN.REFUGE,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.GAS_STATION
        );
        this.buildingSelector = this.getBuildingSelector();
        this.routeSearcher = this.getRouteSearcher();
    }

    public abstract BuildingSelector getBuildingSelector();

    public abstract RouteSearcher getRouteSearcher();

    @Override
    public Message think(int time, ChangeSet changed, MessageManager manager) {

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
                    return FireAction.extinguish(this, time, this.target, this.maxPower);
                }
                else {
                    List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return FireAction.move(this, time, path);
                    }
                    return FireAction.move(this, time, this.routeSearcher.noTargetWalk(time));
                }
            }
            else {
                this.buildingSelector.remove((Building)this.model.getEntity(this.target));
                this.target = this.buildingSelector.getTarget(time);
                if(this.target != null) {
                    List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return FireAction.move(this, time, path);
                    }
                }
                return FireAction.move(this, time, this.routeSearcher.noTargetWalk(time));
            }
        }
        else {
            //if(this.me.isWaterDefined()) { //??????????????????????????????????????????????????????
            if(this.location instanceof Refuge) {
                if (this.me.getWater() < this.maxWater) {
                    return FireAction.rest(this, time);
                }
                else {
                    this.target = this.buildingSelector.getTarget(time);
                    if(this.target != null) {
                        List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                        if(path != null) {
                            path.remove(path.size() - 1);
                            return FireAction.move(this, time, path);
                        }
                    }
                    return FireAction.move(this, time, this.routeSearcher.noTargetWalk(time));
                }
            }
            else {
                this.target = this.buildingSelector.getTarget(time);
                if(this.target != null) {
                    List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                    if(path != null) {
                        path.remove(path.size() - 1);
                        return FireAction.move(this, time, path);
                    }
                }
                return FireAction.move(this, time, this.routeSearcher.noTargetWalk(time));
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

    private Message moveRefuge(int time) {
        Refuge result = null;
        int minDistance = Integer.MAX_VALUE;
        for (Refuge refuge : this.refugeList) {
            int d = RouteUtil.distance(this.model, this.me, refuge);
            if (minDistance >= d) {
                minDistance = d;
                result = refuge;
            }
        }
        List<EntityID> path = this.routeSearcher.getPath(time, this.me, result);
        return path != null ? AmbulanceAction.move(this, time, path) : AmbulanceAction.move(this, time, this.routeSearcher.noTargetWalk(time));
    }
}
