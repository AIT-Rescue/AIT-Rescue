package comlib.adk.team.tactics.basic;

import comlib.adk.team.tactics.AmbulanceTeamTactics;
import comlib.adk.util.action.AmbulanceAction;
import comlib.adk.util.route.RouteSearcher;
import comlib.adk.util.route.RouteUtil;
import comlib.adk.util.target.VictimSelector;
import comlib.manager.MessageManager;
import comlib.message.information.*;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class BasicAmbulance extends AmbulanceTeamTactics{

    public VictimSelector victimSelector;

    public RouteSearcher routeSearcher;

		public ChangeSet changed; // temp add

    @Override
    public void preparation() {
        this.model.indexClass(
                StandardEntityURN.CIVILIAN,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.POLICE_FORCE,
                StandardEntityURN.AMBULANCE_TEAM,
                StandardEntityURN.REFUGE,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.GAS_STATION,
                StandardEntityURN.BUILDING
        );
        this.victimSelector = this.getVictimSelector();
        this.routeSearcher = this.getRouteSearcher();
    }

    public abstract VictimSelector getVictimSelector();

    public abstract RouteSearcher getRouteSearcher();

    @Override
    public Message think(int time, ChangeSet changed, MessageManager manager) {

			this.changed = changed; //temp add

        this.updateInfo(changed, manager);

        if(this.someoneOnBoard()) {
            if (this.location instanceof Refuge) {
                this.target = null;
                return AmbulanceAction.unload(this, time);
            }
            else {
                return this.moveRefuge(time);
            }
        }

        if(this.target != null) {
            Human target = (Human)this.model.getEntity(this.target);
            if(target.getPosition().equals(location.getID())) {
                if ((target instanceof Civilian) && target.getBuriedness() == 0 && !(this.location instanceof Refuge)) {
                    Civilian civilian = (Civilian)target;
                    manager.addSendMessage(new CivilianMessage(civilian));
                    this.victimSelector.remove(civilian);
                    return AmbulanceAction.load(this, time, this.target);
                } else if (target.getBuriedness() > 0) {
                    return AmbulanceAction.rescue(this, time, this.target);
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
                    this.target = this.victimSelector.getTarget(time);
                    if (this.target != null) {
                        List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                        return path != null ? AmbulanceAction.move(this, time, path) : AmbulanceAction.move(this, time, this.routeSearcher.noTargetWalk(time));
                    }
                    else {
                        return AmbulanceAction.move(this, time, this.routeSearcher.noTargetWalk(time));
                    }
                }
            }
            else {
                List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
                return path != null ? AmbulanceAction.move(this, time, path) : AmbulanceAction.move(this, time, this.routeSearcher.noTargetWalk(time));
            }
        }

        if(this.me.getBuriedness() > 0) {
            return AmbulanceAction.rest(this, time);
        }
        this.target = this.victimSelector.getTarget(time);
        if (this.target != null) {
            List<EntityID> path = this.routeSearcher.getPath(time, this.me, this.target);
            return path != null ? AmbulanceAction.move(this, time, path) : AmbulanceAction.move(this, time, this.routeSearcher.noTargetWalk(time));
        }
        List<EntityID> path = this.routeSearcher.noTargetWalk(time);
        return path != null ? AmbulanceAction.move(this, time, path) : AmbulanceAction.rest(this, time);
    }

    private void updateInfo(ChangeSet changed, MessageManager manager) {
        for (EntityID next : changed.getChangedEntities()) {
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
