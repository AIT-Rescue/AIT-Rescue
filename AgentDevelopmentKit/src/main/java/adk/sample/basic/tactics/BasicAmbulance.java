package adk.sample.basic.tactics;

import adk.team.action.*;
import adk.team.util.PositionUtil;
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
    public VictimSelector getVictimSelector() {
        return this.victimSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizeUpdateInfo(updateWorldData, manager);
        if(this.someoneOnBoard()) {
            if (this.location instanceof Refuge) {
                this.target = null;
                return new ActionUnload(this);
            }
            return this.moveRefuge(currentTime);
        }
        if(this.target != null) {
            Human target = (Human)this.model.getEntity(this.target);
            if(target.getPosition().equals(location.getID())) {
                if ((target instanceof Civilian) && target.getBuriedness() == 0 && !(this.location instanceof Refuge)) {
                    Civilian civilian = (Civilian)target;
                    manager.addSendMessage(new CivilianMessage(civilian));
                    this.victimSelector.remove(civilian);
                    return new ActionLoad(this, this.target);
                } else if (target.getBuriedness() > 0) {
                    return new ActionRescue(this, this.target);
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
                        return path != null ? new ActionMove(this, path) : new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
                    }
                    else {
                        return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
                    }
                }
            }
            else {
                List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, this.target);
                return path != null ? new ActionMove(this, path) : new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
            }
        }

        if(this.me.getBuriedness() > 0) {
            return new ActionRest(this);
        }

        this.target = this.victimSelector.getTarget(currentTime);
        List<EntityID> path = null;
        if(this.target != null) {
            path = this.routeSearcher.getPath(currentTime, this.me(), this.target);
        }
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }

    public void organizeUpdateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
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
                    manager.addSendMessage(new BuildingMessage(b));
                }
            }
            /*
            else if(entity instanceof Blockade) {
                //manager.addSendMessage(new RoadMessage(((Blockade)entity)));
            }
            */
        }
    }


    private boolean someoneOnBoard() {
        return this.target != null && (((Human) this.model.getEntity(this.target)).getPosition().getValue() == this.agentID.getValue());
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = PositionUtil.getNearTarget(this.getWorld(), this.me(), this.getRefuges());
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, result);
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }
}
