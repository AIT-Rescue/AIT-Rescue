package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionClear;
import adk.team.action.ActionMove;
import adk.team.tactics.TacticsPolice;
import adk.team.util.BlockadeSelector;
import adk.team.util.RouteSearcher;
import adk.team.util.provider.BlockadeSelectorProvider;
import adk.team.util.provider.RouteSearcherProvider;
import comlib.manager.MessageManager;
import comlib.message.information.BuildingMessage;
import comlib.message.information.CivilianMessage;
import rescuecore2.config.Config;
import rescuecore2.misc.geometry.GeometryTools2D;
import rescuecore2.misc.geometry.Line2D;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class BasicPolice extends TacticsPolice implements RouteSearcherProvider, BlockadeSelectorProvider {

    public BlockadeSelector blockadeSelector;

    public RouteSearcher routeSearcher;

    public ChangeSet changed; // temp add

    @Override
	public void preparation(Config config) {
		this.routeSearcher = this.initRouteSearcher();
		this.blockadeSelector = this.initBlockadeSelector();
	}

    public abstract BlockadeSelector initBlockadeSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public Action think(int time, ChangeSet changed, MessageManager manager) {

        this.changed = changed; //temp add

        this.updateInfo(changed, manager);
        // if(this.util == null) {
        this.target = this.blockadeSelector.getTarget(time);
        // }
        Blockade blockade = (Blockade)this.model.getEntity(this.target);

		if (blockade != null) {
			List<Line2D> lines = GeometryTools2D.pointsToLines(GeometryTools2D.vertexArrayToPoints(blockade.getApexes()), true);
			double best = Double.MAX_VALUE;
			best = 6000.0; //MAGIC
			Point2D bestPoint = null;
			Point2D origin = new Point2D(this.me.getX(), this.me.getY());
			for (Line2D next : lines) {
				Point2D closest = GeometryTools2D.getClosestPointOnSegment(next, origin);
				double d = GeometryTools2D.getDistance(origin, closest);
				if (d < best) {
					best = d;
					bestPoint = closest;
				}
			}

			if(bestPoint != null) {
				Vector2D v = bestPoint.minus(new Point2D(this.me.getX(), this.me.getY()));
				v = v.normalised().scale(1000000);
				this.blockadeSelector.remove(blockade);
				return new ActionClear(this, time, blockade, (int) (this.me.getX() + v.getX()), (int) (this.me.getY() + v.getY()));
			}

			List<EntityID> path = this.routeSearcher.getPath(time, me().getPosition(), blockade.getPosition());
			if (path != null) {
				return new ActionMove(this, time, path, blockade.getX(), blockade.getY());
			}
		}
            // Plan a path to a blocked area
                // Road r = (Road)model.getEntity(path.get(path.size() - 1));
                // Blockade b = getTargetBlockade(r, -1);

		return new ActionMove(this, time, this.routeSearcher.noTargetWalk(time));
	}

    private void updateInfo(ChangeSet changed, MessageManager manager) {
        for (EntityID next : changed.getChangedEntities()) {
            StandardEntity entity = this.model.getEntity(next);
            if(entity instanceof Civilian) {
                Civilian civilian = (Civilian)entity;
                if(civilian.getBuriedness() > 0) {
                    manager.addSendMessage(new CivilianMessage(civilian));
                }
            }
            else if(entity instanceof Blockade) {
                this.blockadeSelector.add((Blockade)entity);
            }
            else if(entity instanceof Building) {
                Building b = (Building)entity;
                if(b.isOnFire()) {
                    manager.addSendMessage(new BuildingMessage(b));
                }
            }
        }
    }

    @Override
    public BlockadeSelector getBlockadeSelector() {
        return this.blockadeSelector;
    }

    @Override
    public RouteSearcher getRouteSearcher() {
        return this.routeSearcher;
    }
}
