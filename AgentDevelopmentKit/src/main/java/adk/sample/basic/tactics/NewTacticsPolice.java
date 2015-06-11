package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionClear;
import adk.team.action.ActionMove;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsPolice;
import adk.team.util.ImpassableSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import comlib.message.information.MessagePoliceForce;
import rescuecore2.config.Config;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.Area;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.List;

public abstract class NewTacticsPolice extends TacticsPolice implements RouteSearcherProvider, ImpassableSelectorProvider {

    public ImpassableSelector impassableSelector;
    public RouteSearcher routeSearcher;

    public boolean beforeMove;

    public ClearPlanner clear;
    private List<EntityID> clearPath;
    private List<EntityID> beforeMovePath;

    @Override
    public void preparation(Config config, MessageManager messageManager) {
        this.routeSearcher = this.initRouteSearcher();
        this.impassableSelector = this.initImpassableSelector();
        this.beforeMove = false;

        this.clear = new ClearPlanner(this.world);
        this.beforeMovePath = new ArrayList<>();
    }

    public abstract ImpassableSelector initImpassableSelector();

    public abstract RouteSearcher initRouteSearcher();

    public abstract void organizeUpdateInfo(int currentTime, ChangeSet updateWorldInfo, MessageManager manager);

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }

    @Override
    public ImpassableSelector getImpassableSelector() {
        return this.impassableSelector;
    }

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.organizeUpdateInfo(currentTime, updateWorldData, manager);
        if (this.me.getBuriedness() > 0) {
            return this.buriednessAction(manager);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.checkClearPath();
        if(this.target == null || this.clearPath == null || this.clearPath.isEmpty()) {
            this.target = this.impassableSelector.getNewTarget(currentTime);
            if(this.target == null) {
                this.beforeMove = true;
                this.beforeMovePath = this.routeSearcher.noTargetMove(currentTime, this.me);
                return new ActionMove(this, this.beforeMovePath);
            }
            this.clearPath = this.routeSearcher.getPath(currentTime, this.me(), this.target);
            if(this.clearPath == null || this.clearPath.isEmpty()) {
                this.beforeMove = true;
                this.beforeMovePath = this.routeSearcher.noTargetMove(currentTime, this.me);
                return new ActionMove(this, this.beforeMovePath);
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return new ActionRest(this);
    }

    private void checkClearPath() {
        if(this.target != null && clearPath != null && !clearPath.isEmpty()) {
            if(this.target.getValue() == this.me().getPosition().getValue()) {
                this.target = null;
                this.clearPath = null;
            }
            else {
                int index = this.clearPath.indexOf(this.me().getPosition());
                if (index != -1) {
                    for (int i = 0; i < index; i++) {
                        EntityID removeID = this.clearPath.remove(i);
                        this.impassableSelector.remove(removeID);
                    }
                }
            }
        }
    }













    public Action oldTthink(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        if(beforeMove) {
            int index = this.beforeMovePath.indexOf(this.me().getPosition());
            if (index != -1) {
                for (int i = 0; i < index; i++) {
                    this.impassableSelector.remove(this.beforeMovePath.get(i));
                }
            }
        }
        else {
            if(clearPath != null) {
                int index = this.clearPath.indexOf(this.me().getPosition());
                if (index != -1) {
                    for (int i = 0; i < index; i++) {
                        this.clearPath.remove(i);
                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(target == null || clearPath == null || clearPath.isEmpty()) {
            this.target = this.impassableSelector.getNewTarget(currentTime);
            if(this.target != null) {
                this.clearPath = this.routeSearcher.getPath(currentTime, this.me, this.target);
                if(this.clearPath == null || this.clearPath.isEmpty()) {
                    this.beforeMove = true;
                    this.beforeMovePath = this.routeSearcher.noTargetMove(currentTime, this.me);
                    return new ActionMove(this, this.beforeMovePath);
                }
            }
            else {
                this.beforeMove = true;
                this.beforeMovePath = this.routeSearcher.noTargetMove(currentTime, this.me);
                return new ActionMove(this, this.beforeMovePath);
            }

        }

        this.beforeMovePath = this.routeSearcher.noTargetMove(currentTime, this.me);
        return new ActionMove(this, this.beforeMovePath);
    }

    public int count;

    private Action buriednessAction(MessageManager manager) {
        this.beforeMove = false;
        manager.addSendMessage(new MessagePoliceForce(this.me, MessagePoliceForce.ACTION_REST, this.agentID));
        List<EntityID> neighbours = ((Area)this.location).getNeighbours();
        if(neighbours.isEmpty()) {
            return new ActionRest(this);
        }
        if(this.count <= 0) {
            this.count = neighbours.size();
        }
        this.count--;
        Area area = (Area)this.world.getEntity(neighbours.get(this.count));
        Vector2D vector = (new Point2D(area.getX(), area.getY())).minus(new Point2D(this.me.getX(), this.me.getY())).normalised().scale(1000000);
        return new ActionClear(this, (int) (this.me.getX() + vector.getX()), (int) (this.me.getY() + vector.getY()));
    }
}
