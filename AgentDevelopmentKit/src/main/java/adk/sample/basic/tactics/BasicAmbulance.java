package adk.sample.basic.tactics;

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

    public abstract void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager);

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        //情報の整理
        this.organizeUpdateInfo(currentTime, updateWorldData, manager);
        //自分の状態チェック
        if(this.me.getBuriedness() > 0) {
            this.target = null;
            //自分自身をRescueできるのか？？
            //return new ActionRest(this);
            return new ActionRescue(this, this.agentID);
        }
        //人を載せているか or 回復中
        if(this.location instanceof Refuge) {
            this.target = null;
            if(this.someoneOnBoard()) {
                return new ActionUnload(this);
            }
            if(this.me.getHP() < 5000) {
                return new ActionRest(this);
            }
        }
        //避難所への移動条件
        if(this.someoneOnBoard() || this.me.getHP() < 1000) {
            return this.moveRefuge(currentTime);
        }
        //対象の選択・切り替え
        this.target = this.target == null ? this.victimSelector.getNewTarget(currentTime) : this.victimSelector.updateTarget(currentTime, this.target);
        if(this.target == null) {
            return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
        }
        //救助開始
        do {
            Human victim = (Human) this.world.getEntity(this.target);
            if (victim.getPosition().getValue() != this.location.getID().getValue()) {
                return this.moveTarget(currentTime);
            }
            if (victim.getBuriedness() > 0) {
                return new ActionRescue(this, this.target);
            } else {
                if (victim instanceof Civilian) {
                    Civilian civilian = (Civilian) victim;
                    manager.addSendMessage(new MessageCivilian(civilian));
                    this.victimSelector.remove(civilian);
                    return new ActionLoad(this, this.target);
                }
                if (victim instanceof AmbulanceTeam) {
                    AmbulanceTeam ambulanceTeam = (AmbulanceTeam) victim;
                    manager.addSendMessage(new MessageAmbulanceTeam(ambulanceTeam));
                    this.victimSelector.remove(ambulanceTeam);
                } else if (victim instanceof FireBrigade) {
                    FireBrigade fireBrigade = (FireBrigade) victim;
                    manager.addSendMessage(new MessageFireBrigade(fireBrigade));
                    this.victimSelector.remove(fireBrigade);
                } else if (victim instanceof PoliceForce) {
                    PoliceForce policeForce = (PoliceForce) victim;
                    manager.addSendMessage(new MessagePoliceForce(policeForce));
                    this.victimSelector.remove(policeForce);
                }
            }
            //対象が救助済み．または対象外の場合
            this.target = this.victimSelector.getNewTarget(currentTime);
        }while (this.target != null);
        return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime));
    }

    public boolean someoneOnBoard() {
        return this.target != null && ((Human)this.getWorld().getEntity(this.target)).getPosition().getValue() == this.getID().getValue();
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = PositionUtil.getNearTarget(this.world, this.me, this.getRefuges());
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, result);
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }

    public Action moveTarget(int currentTime) {
        List<EntityID> path = null;
        if(this.target != null) {
            path = this.routeSearcher.getPath(currentTime, this.me, this.target);
        }
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime));
    }
}
