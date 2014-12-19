package adk.sample.newbasic.tactics;

import adk.team.action.*;
import adk.team.tactics.TacticsAmbulance;
import adk.team.util.RouteSearcher;
import adk.team.util.VictimSelector;
import adk.team.util.graph.PositionUtil;
import adk.team.util.provider.RouteSearcherProvider;
import adk.team.util.provider.VictimSelectorProvider;
import comlib.manager.MessageManager;
import comlib.message.information.*;
import rescuecore2.config.Config;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class NewBasicAmbulance  extends TacticsAmbulance implements RouteSearcherProvider, VictimSelectorProvider {

    public VictimSelector victimSelector;

    public RouteSearcher routeSearcher;

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_CIVILIAN = 1;
    public static final int TYPE_AGENT_AMBULANCE = 2;
    public static final int TYPE_AGENT_FIRE = 3;
    public static final int TYPE_AGENT_POLICE = 4;

    public int targetType;

    public EntityID oldTarget;

    @Override
    public void preparation(Config config) {
        this.victimSelector = this.initVictimSelector();
        this.routeSearcher = this.initRouteSearcher();
        this.targetType = TYPE_UNKNOWN;
        this.oldTarget = this.getID();
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

    public abstract void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager);

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        //情報の整理
        this.organizeUpdateInfo(currentTime, updateWorldData, manager);
        //自分の状態チェック
        if(this.me().getBuriedness() > 0) {
            this.target = null;
            //自分自身をRescueできるのか？？
            //return new ActionRest(this);
            return new ActionRescue(this, this.getID());
        }
        //人を載せているか or 回復中
        if(this.location() instanceof Refuge) {
            this.target = null;
            if(this.someoneOnBoard()) {
                return new ActionUnload(this);
            }
            if(this.me().getHP() < 5000) {
                return new ActionRest(this);
            }
        }
        //避難所への移動条件
        if(this.someoneOnBoard() || this.me().getHP() < 1000) {
            return this.moveRefuge(currentTime);
        }
        //対象の選択・切り替え
        this.target = this.target == null ? this.victimSelector.getNewTarget(currentTime) : this.victimSelector.updateTarget(currentTime, this.target);
        if(this.target == null) {
            this.oldTarget = this.getID();
            return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
        }
        if(this.target.getValue() != this.oldTarget.getValue()) {
            this.oldTarget = this.target;
            this.resetTargetInfo();
        }
        //救助開始
        Human victim = (Human)this.getWorld().getEntity(this.target);
        if(victim.getPosition().getValue() != this.location().getID().getValue()) {
            return this.moveTarget(currentTime);
        }
        if(this.targetType != TYPE_UNKNOWN) {
            if(victim.getBuriedness() > 0) {
                return new ActionRescue(this, this.target);
            }
            else {
                if(this.targetType == TYPE_CIVILIAN) {
                    Civilian civilian = (Civilian)victim;
                    manager.addSendMessage(new MessageCivilian(civilian));
                    this.victimSelector.remove(civilian);
                    return new ActionLoad(this, this.target);
                }
                if(this.targetType == TYPE_AGENT_AMBULANCE) {
                    AmbulanceTeam ambulanceTeam = (AmbulanceTeam)victim;
                    manager.addSendMessage(new MessageAmbulanceTeam(ambulanceTeam));
                    this.victimSelector.remove(ambulanceTeam);
                }
                else if(this.targetType == TYPE_AGENT_FIRE) {
                    FireBrigade fireBrigade = (FireBrigade)victim;
                    manager.addSendMessage(new MessageFireBrigade(fireBrigade));
                    this.victimSelector.remove(fireBrigade);
                }
                else if(this.targetType == TYPE_AGENT_POLICE) {
                    PoliceForce policeForce = (PoliceForce)victim;
                    manager.addSendMessage(new MessagePoliceForce(policeForce));
                    this.victimSelector.remove(policeForce);
                }
            }
        }
        this.target = this.victimSelector.getNewTarget(currentTime);
        if(this.target != null) {
            this.oldTarget = this.target;
            this.resetTargetInfo();
        }
        return this.moveTarget(currentTime);
    }

    public void resetTargetInfo() {
        StandardEntity entity = this.getWorld().getEntity(this.target);
        if(entity == null) {
            this.targetType = TYPE_UNKNOWN;
        }
        else if(entity instanceof Civilian) {
            this.targetType = TYPE_CIVILIAN;
        }
        else if(entity instanceof AmbulanceTeam) {
            this.targetType = TYPE_AGENT_AMBULANCE;
        }
        else if(entity instanceof FireBrigade) {
            this.targetType = TYPE_AGENT_FIRE;
        }
        else if(entity instanceof PoliceForce) {
            this.targetType = TYPE_AGENT_POLICE;
        }
        else {
            this.targetType = TYPE_UNKNOWN;
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
