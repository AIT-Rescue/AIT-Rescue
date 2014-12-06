package sample.tactics;

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
import sample.event.SampleAmbulanceEvent;
import sample.event.SampleCivilianEvent;
import sample.event.SampleFireEvent;
import sample.event.SamplePoliceEvent;
import sample.util.SampleRouteSearcher;
import sample.util.SampleVictimSelector;

import java.util.List;

public class SampleAmbulance extends TacticsAmbulance implements RouteSearcherProvider, VictimSelectorProvider {

    public VictimSelector victimSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config) {
        this.victimSelector = new SampleVictimSelector(this);
        this.routeSearcher = new SampleRouteSearcher(this);
    }

    @Override
    public String getTacticsName() {
        return "Sample";
    }

    @Override
    public void registerEvent(MessageManager manager) {
        manager.registerEvent(new SampleCivilianEvent(this, this));
        manager.registerEvent(new SampleAmbulanceEvent(this, this));
        manager.registerEvent(new SampleFireEvent(this, this));
        manager.registerEvent(new SamplePoliceEvent(this, this));
    }

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
            if (this.location() instanceof Refuge) {
                this.target = null;
                return new ActionUnload(this);
            }
            return this.moveRefuge(currentTime);
        }
        if(this.me().getBuriedness() > 0) {
            //自分自身をRescueできるのか？？
            //return new ActionRest(this);
            return new ActionRescue(this, this.getID());
        }
        //HPっていったいいくつなんだ
        if(this.me().getHP() < 100) {
            this.target = null;
            return this.location() instanceof Refuge ? new ActionRest(this) : this.moveRefuge(currentTime);
        }
        if(this.target == null) {
            this.target = this.victimSelector.getTarget(currentTime);
            if(this.target == null) {
                return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
            }
        }
        Human victim = (Human)this.getWorld().getEntity(this.target);
        if(victim.getPosition().getValue() != this.location().getID().getValue()) {
            List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me(), this.target);
            return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
        }
        else {
            if ((victim instanceof Civilian) && victim.getBuriedness() == 0) {
                Civilian civilian = (Civilian)victim;
                manager.addSendMessage(new CivilianMessage(civilian));
                this.victimSelector.remove(civilian);
                return new ActionLoad(this, this.target);
            }
            else if (victim.getBuriedness() > 0) {
                return new ActionRescue(this, this.target);
            }
            if(victim instanceof AmbulanceTeam) {
                AmbulanceTeam ambulanceTeam = (AmbulanceTeam)victim;
                manager.addSendMessage(new AmbulanceTeamMessage(ambulanceTeam));
                this.victimSelector.remove(ambulanceTeam);
            }
            else if(victim instanceof FireBrigade) {
                FireBrigade fireBrigade = (FireBrigade)victim;
                manager.addSendMessage(new FireBrigadeMessage(fireBrigade));
                this.victimSelector.remove(fireBrigade);
            }
            else if(victim instanceof PoliceForce) {
                PoliceForce policeForce = (PoliceForce)victim;
                manager.addSendMessage(new PoliceForceMessage(policeForce));
                this.victimSelector.remove(policeForce);
            }
            this.target = this.victimSelector.getTarget(currentTime);
            return this.moveTarget(currentTime);
        }
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
        }
    }

    public boolean someoneOnBoard() {
        return this.target != null && ((Human)this.getWorld().getEntity(this.target)).getPosition().getValue() == this.getID().getValue();
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = PositionUtil.getNearTarget(this.getWorld(), this.me(), this.getRefuges());
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me(), result);
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }

    public Action moveTarget(int currentTime) {
        List<EntityID> path = null;
        if(this.target != null) {
            path = this.routeSearcher.getPath(currentTime, this.me(), this.target);
        }
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }
}
