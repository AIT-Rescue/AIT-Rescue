package sample.tactics;

import adk.team.util.RouteSearcher;
import adk.team.util.VictimSelector;
import adk.team.action.*;
import adk.team.tactics.TacticsAmbulance;
import adk.team.util.graph.PositionUtil;
import adk.team.util.graph.RouteManager;
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

public class SampleTacticsAmbulance extends TacticsAmbulance implements RouteSearcherProvider, VictimSelectorProvider {

    public VictimSelector victimSelector;

    public RouteSearcher routeSearcher;

    @Override
    public void preparation(Config config) {
        this.victimSelector = new SampleVictimSelector(this);
        this.routeSearcher = new SampleRouteSearcher(this, new RouteManager(this.world));
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
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        //情報の整理
        this.organizeUpdateInfo(currentTime, updateWorldData, manager);
        //自分の状態チェック
        if(this.me.getBuriedness() > 0) {
            manager.addSendMessage(new MessageAmbulanceTeam(this.me, MessageAmbulanceTeam.ACTION_REST, this.agentID));
            this.target = null;
            return new ActionRest(this);
        }
        //人を載せているか or 回復中
        if(this.location instanceof Refuge) {
            this.target = null;
            if(this.someoneOnBoard()) {
                return new ActionUnload(this);
            }
            if(this.me.getDamage() > 0) {
                return new ActionRest(this);
            }
        }
        //避難所への移動条件
        if(this.someoneOnBoard() || this.me.getDamage() >= 300) {
            return this.moveRefuge(currentTime);
        }
        //対象の選択・切り替え
        this.target = this.target == null ? this.victimSelector.getNewTarget(currentTime) : this.victimSelector.updateTarget(currentTime, this.target);
        if(this.target == null) {
            return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime, this.me));
        }
        //救助開始
        do {
            Human victim = (Human) this.world.getEntity(this.target);
            if (victim.getPosition().getValue() != this.location.getID().getValue()) {
                return this.moveTarget(currentTime);
            }
            if (victim.getBuriedness() > 0) {
                return new ActionRescue(this, this.target);
            }
            //救助済みの場合
            //市民
            if (victim instanceof Civilian) {
                Civilian civilian = (Civilian) victim;
                manager.addSendMessage(new MessageCivilian(civilian));
                this.victimSelector.remove(civilian);
                return new ActionLoad(this, this.target);
            }
            //災害救助エージェント
            if (victim instanceof AmbulanceTeam) {
                AmbulanceTeam ambulanceTeam = (AmbulanceTeam) victim;
                manager.addSendMessage(new MessageAmbulanceTeam(ambulanceTeam, MessageAmbulanceTeam.ACTION_REST, null));
                this.victimSelector.remove(ambulanceTeam);
            } else if (victim instanceof FireBrigade) {
                FireBrigade fireBrigade = (FireBrigade) victim;
                manager.addSendMessage(new MessageFireBrigade(fireBrigade, MessageFireBrigade.ACTION_REST, null));
                this.victimSelector.remove(fireBrigade);
            } else if (victim instanceof PoliceForce) {
                PoliceForce policeForce = (PoliceForce) victim;
                manager.addSendMessage(new MessagePoliceForce(policeForce, MessagePoliceForce.ACTION_REST, null));
                this.victimSelector.remove(policeForce);
            }
            //対象が救助済み．または対象外の場合
            this.target = this.victimSelector.getNewTarget(currentTime);
        }while (this.target != null);
        return new ActionMove(this, this.routeSearcher.noTargetMove(currentTime, this.me));
    }

    public void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager) {
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
                    manager.addSendMessage(new MessageBuilding(b));
                }
            }
            else if(entity instanceof Blockade) {
                Blockade blockade = (Blockade) entity;
                manager.addSendMessage(new MessageRoad((Road)this.world.getEntity(blockade.getPosition()), blockade, false));
            }
        }
    }

    public boolean someoneOnBoard() {
        return this.target != null && ((Human)this.world.getEntity(this.target)).getPosition().getValue() == this.agentID.getValue();
    }

    public Action moveRefuge(int currentTime) {
        Refuge result = PositionUtil.getNearTarget(this.world, this.me, this.getRefuges());
        List<EntityID> path = this.routeSearcher.getPath(currentTime, this.me, result);
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime, this.me));
    }

    public Action moveTarget(int currentTime) {
        List<EntityID> path = null;
        if(this.target != null) {
            path = this.routeSearcher.getPath(currentTime, this.me, this.target);
        }
        return new ActionMove(this, path != null ? path : this.routeSearcher.noTargetMove(currentTime, this.me));
    }
}
