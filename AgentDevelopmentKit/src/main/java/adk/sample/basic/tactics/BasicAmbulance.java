package adk.sample.basic.tactics;

import adk.team.action.*;
import adk.team.util.RouteSearcher;
import adk.team.tactics.TacticsAmbulance;
import adk.team.util.VictimSelector;
import adk.team.util.provider.RouteSearcherProvider;
import adk.team.util.provider.VictimSelectorProvider;
import comlib.manager.MessageManager;
import comlib.message.information.*;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class BasicAmbulance extends TacticsAmbulance implements RouteSearcherProvider, VictimSelectorProvider {

    public VictimSelector victimSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config) {
        this.victimSelector = this.initVictimSelector();
        this.routeSearcher = this.initRouteSearcher();
    }

    public abstract VictimSelector initVictimSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizingUpdateInfo(updateWorldData, manager);

        if(this.someoneOnBoard()) {
            if (this.location instanceof Refuge) {
                this.target = null;
                return new ActionUnload(this, currentTime);
            }
            else {
                return this.moveRefuge(currentTime);
            }
        }

        if(this.target != null) {
            Human target = (Human)this.model.getEntity(this.target);
            if(target.getPosition().equals(location.getID())) {
                if ((target instanceof Civilian) && target.getBuriedness() == 0 && !(this.location instanceof Refuge)) {
                    Civilian civilian = (Civilian)target;
                    manager.addSendMessage(new CivilianMessage(civilian));
                    this.victimSelector.remove(civilian);
                    return new ActionLoad(this, currentTime, this.target);
                } else if (target.getBuriedness() > 0) {
                    return new ActionRescue(this, currentTime, this.target);
                }
                else {
                    if(target instanceof Civilian) {
                        Civilian civilian = (Civilian)target;
                        manager.addSendMessage(new CivilianMessage(civilian));
                        this.victimSelector.remove(civilian);
                    }
                    if(target instanceof AmbulanceTeam) {
                        AmbulanceTeam ambulanceTeam = (AmbulanceTeam)target;
                        manager.addSendMessage(new AmbulanceTeamMessage(ambulanceTeam));
                        this.victimSelector.remove(target);
                    }
                    if(target instanceof FireBrigade) {
                        FireBrigade fireBrigade = (FireBrigade)target;
                        manager.addSendMessage(new FireBrigadeMessage(fireBrigade));
                        this.victimSelector.remove(target);
                    }
                    if(target instanceof PoliceForce) {
                        PoliceForce policeForce = (PoliceForce)target;
                        manager.addSendMessage(new PoliceForceMessage(policeForce));
                        this.victimSelector.remove(target);
                    }
                    this.target = this.victimSelector.getTarget(currentTime);
                    if (this.target != null) {
                        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                        return path != null ? new ActionMove(this, currentTime, path) : new ActionMove(this, currentTime, this.routeSearcher.noTargetWalk(currentTime));
                    }
                    else {
                        return new ActionMove(this, currentTime, this.routeSearcher.noTargetWalk(currentTime));
                    }
                }
            }
            else {
                List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                return path != null ? new ActionMove(this, currentTime, path) : new ActionMove(this, currentTime, this.routeSearcher.noTargetWalk(currentTime));
            }
        }

        if(this.me.getBuriedness() > 0) {
            return new ActionRest(this, currentTime);
        }
        this.target = this.victimSelector.getTarget(currentTime);
        if (this.target != null) {
            List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
            return path != null ? new ActionMove(this, currentTime, path) : new ActionMove(this, currentTime, this.routeSearcher.noTargetWalk(currentTime));
        }
        List<EntityID> path = this.routeSearcher.noTargetWalk(currentTime);
        return path != null ? new ActionMove(this, currentTime, path) : new ActionRest(this, currentTime);
    }

    private void organizingUpdateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
            StandardEntity entity = model.getEntity(next);
            if(entity instanceof Civilian) {
                this.victimSelector.add((Civilian) entity);
            }
            else if(entity instanceof Human) {
                this.victimSelector.add((Human)entity);
            }
            else if(entity instanceof Blockade) {
                manager.addSendMessage(new RoadMessage(((Blockade)entity)));
            }
            else if(entity instanceof Building) {
                Building b = (Building)entity;
                if(b.isOnFire()) {
                    manager.addSendMessage(new BuildingMessage(b));
                }
            }
        }
    }


    private boolean someoneOnBoard() {
        return this.target != null && ((Human) this.model.getEntity(this.target)).getPosition().equals(this.agentID);
    }

    private Action moveRefuge(int currentTime) {
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
        return path != null ? new ActionMove(this, currentTime, path) : new ActionMove(this, currentTime, this.routeSearcher.noTargetWalk(currentTime));
    }


    @Override
    public VictimSelector getVictimSelector() {
        return this.victimSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }
}
