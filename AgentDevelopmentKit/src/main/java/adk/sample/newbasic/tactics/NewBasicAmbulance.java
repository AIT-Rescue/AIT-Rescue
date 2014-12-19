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

    public EntityID oldTarget;

    @Override
    public void preparation(Config config) {
        this.victimSelector = this.initVictimSelector();
        this.routeSearcher = this.initRouteSearcher();
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

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizeUpdateInfo(updateWorldData, manager);
        // 人を載せて移動中の時
        /*if(this.someoneOnBoard()) {
            if(this.location() instanceof Refuge) {
                this.target = null;
                return new ActionUnload(this);
            }
            return this.moveRefuge(currentTime);
        }*/
        //自分の状態チェック
        if(this.me().getBuriedness() > 0) {
            //自分自身をRescueできるのか？？
            //return new ActionRest(this);
            return new ActionRescue(this, this.getID());
        }
        //HP -> 10000
        /*if(this.me().getHP() < 1000) {
            this.target = null;
            return this.location() instanceof Refuge ? new ActionRest(this) : this.moveRefuge(currentTime);
        }*/
        if(this.location() instanceof Refuge) {
            if(this.someoneOnBoard()) {
                this.target = null;
                return new ActionUnload(this);
            }
            if(this.me().getHP() < 5000) {
                return new ActionRest(this);
            }
        }
        if(this.someoneOnBoard() || this.me().getHP() < 1000) {
            return this.moveRefuge(currentTime);
        }
        //対象の選択・切り替え
        this.target = this.target == null ? this.getVictimSelector().getNewTarget(currentTime) : this.getVictimSelector().updateTarget(this.target);
        if(this.target == null) {
            return new ActionMove(this, this.getRouteSearcher().noTargetMove(currentTime));
        }
        if(this.target.getValue() != this.oldTarget.getValue()) {
            this.oldTarget = this.target;
            this.resetTargetInfo();
        }

        return new ActionRest(this);
    }

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_CIVILIAN = 1;
    public static final int TYPE_AGENT = 2;

    //0 -> unknown
    //1 -> civilian
    //2 -> agent
    public int targetType = TYPE_UNKNOWN;

    public void resetTargetInfo() {
        StandardEntity entity = this.getWorld().getEntity(this.target);
        if(entity instanceof Civilian) {
            this.targetType = TYPE_CIVILIAN;
        }
        else if(entity instanceof Human) {
            this.targetType = TYPE_AGENT;
        }
        else {
            this.targetType = TYPE_UNKNOWN;
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
                    manager.addSendMessage(new MessageBuilding(b));
                }
            }
            /*
            else if(entity instanceof Blockade) {
                //manager.addSendMessage(new RoadMessage(((Blockade)entity)));
            }
            */
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
