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
import rescuecore2.misc.Pair;
import rescuecore2.misc.geometry.GeometryTools2D;
import rescuecore2.misc.geometry.Line2D;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.misc.geometry.Vector2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasicPolice extends TacticsPolice implements RouteSearcherProvider, BlockadeSelectorProvider {

    public BlockadeSelector blockadeSelector;

    public RouteSearcher routeSearcher;

    @Override
	public void preparation(Config config) {
		this.routeSearcher = this.initRouteSearcher();
		this.blockadeSelector = this.initBlockadeSelector();
        //
        this.passableEdgeMap = new HashMap<>();
        this.isClearTime = false;
        this.targetRoad = null;
        this.targetPoints = null;
	}

    public abstract BlockadeSelector initBlockadeSelector();

    public abstract RouteSearcher initRouteSearcher();

    @Override
    public Action think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.agentPosition = new Point2D(this.me().getX(), this.me().getY());
        this.updateInfo(updateWorldData, manager);
        // if(this.util == null) {
        this.target = this.blockadeSelector.getTarget(currentTime);
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
                //回転とか求める必要なし．ただの2点の差
				Vector2D v = bestPoint.minus(new Point2D(this.me.getX(), this.me.getY()));
                //ここで対象との差を変更して，一定の長さの距離にしている
				v = v.normalised().scale(1000000);
				this.blockadeSelector.remove(blockade);
				return new ActionClear(this, currentTime, blockade, (int) (this.me.getX() + v.getX()), (int) (this.me.getY() + v.getY()));
			}

			List<EntityID> path = this.routeSearcher.getPath(currentTime, me().getPosition(), blockade.getPosition());
			if (path != null) {
				return new ActionMove(this, currentTime, path, blockade.getX(), blockade.getY());
			}
		}
            // Plan a path to a blocked area
                // Road r = (Road)model.getEntity(path.get(path.size() - 1));
                // Blockade b = getTargetBlockade(r, -1);

		return new ActionMove(this, currentTime, this.routeSearcher.noTargetWalk(currentTime));
	}

    private void updateInfo(ChangeSet updateWorldInfo, MessageManager manager) {
        for (EntityID next : updateWorldInfo.getChangedEntities()) {
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

    public Map<EntityID, List<Edge>> passableEdgeMap;

    public List<Edge> getPassableEdge(Blockade blockade) {
        EntityID roadID = blockade.getPosition();
        List<Edge> edges = this.passableEdgeMap.get(roadID);
        return edges != null ? edges : this.getPassableEdge((Road) this.getWorld().getEntity(roadID));
    }

    public List<Edge> getPassableEdge(Road road) {
        EntityID roadID = road.getID();
        List<Edge> edges = this.passableEdgeMap.get(roadID);
        if(edges == null) {
            edges = new ArrayList<>();
            for(Edge edge : road.getEdges()) {
                if(edge.isPassable()) {
                    edges.add(edge);
                }
            }
            this.passableEdgeMap.put(roadID, edges);
        }
        return edges;
    }

    public boolean isClearTime;

    public Point2D agentPosition;

    public Road targetRoad;
    public List<Point2D> targetPoints;

    public List<Point2D> getClearPointArray(Blockade blockade) {
        return this.getClearPointArray(this.getPassableEdge(blockade));
    }

    public List<Point2D> getClearPointArray(Road road) {
        return this.getClearPointArray(this.getPassableEdge(road));
    }

    public List<Point2D> getClearPointArray(List<Edge> edges) {
        int size = edges.size();
        List<Point2D> points = new ArrayList<>();
        for (Edge edge : edges) {
            points.add(new Point2D(((edge.getStartX() + edge.getEndX()) / 2), ((edge.getStartY() + edge.getEndY()) / 2)));
        }
        return points;
    }

    public Point2D getTargetPosition(AmbulanceTeam ambulance, Road road) {
        EntityID roadID = road.getID();
        if(!roadID.equals(ambulance.getPosition())) {
            return null;
        }
        List<Edge> edges = this.getPassableEdge(road);
        if(edges.isEmpty()) {
            return null;
        }

        return null;
    }


}
