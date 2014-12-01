package adk.sample.basic.tactics;

import adk.team.action.Action;
import adk.team.action.ActionClear;
import adk.team.action.ActionMove;
import adk.team.action.ActionRest;
import adk.team.tactics.TacticsPolice;
import adk.team.util.BlockadeSelector;
import adk.team.util.PositionUtil;
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

import java.util.*;
import java.util.List;

public abstract class BasicPolice extends TacticsPolice implements RouteSearcherProvider, BlockadeSelectorProvider {

    public BlockadeSelector blockadeSelector;

    public RouteSearcher routeSearcher;

    @Override
	public void preparation(Config config) {
		this.routeSearcher = this.initRouteSearcher();
		this.blockadeSelector = this.initBlockadeSelector();
        //
        this.passableEdgeMap = new HashMap<>();
        this.passablePointMap = new HashMap<>();
        this.allEdgePointMap = new HashMap<>();
        this.clearTargetPointMap = new HashMap<>();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Action newThink(int currentTime, ChangeSet updateWorldData, MessageManager manager) {
        this.agentPosition = new Point2D(this.me().getX(), this.me().getY());
        EntityID roadID = this.me().getPosition();
        Road road = (Road)this.getWorld().getEntity(roadID);
        if(!roadID.equals(this.target)) {
            if(this.passable(road)) {
                return new ActionMove(this, currentTime, this.getRouteSearcher().getPath(currentTime, this.getID(), this.target));
            }
            this.target = roadID;
        }
        if(this.mainTargetPosition == null) {
            List<Point2D> clearTargetPoint = this.getClearTargetPoint(road);
            this.count = clearTargetPoint.size();
        }
        if(this.count != 0) {
            this.count--;
            this.mainTargetPosition = this.getClearTargetPoint(road).get(this.count);
            Vector2D vector = this.getVector(this.agentPosition, this.mainTargetPosition, road.getEdges());
        }

        return new ActionRest(this, currentTime);
    }

    public Vector2D getVector(Point2D agentPos, Point2D targetPos, List<Edge> edges) {
        if(this.canStraightForward(agentPos, targetPos, edges)) {
            return targetPos.minus(agentPos).normalised().scale(1000000);
        }
        else {
            List<Point2D> points = new ArrayList<>();
            Point2D edgePoint = null;
            Vector2D vector = null;
            for (Edge edge : edges) {
                edgePoint = this.getEdgePoint(edge);
                if (this.canStraightForward(agentPos, edgePoint, edges)) {
                    points.add(edgePoint);
                }
            }
            if (points.isEmpty()) {
                return null;
            }
            return PositionUtil.getNearPosition(targetPos, points).minus(agentPos).normalised().scale(1000000);
        }
    }

    public Point2D getNearPosition(Point2D position, Collection<Point2D> targets) {
        Point2D result = null;
        for(Point2D target : targets) {
            result = (result != null) ? PositionUtil.compareDistance(position, result, target) : target;
        }
        return result;
    }

    public Map<EntityID, List<Edge>> passableEdgeMap;
    public Map<EntityID, List<Point2D>> passablePointMap;
    public Map<EntityID, List<Point2D>> allEdgePointMap;
    public Map<EntityID, List<Point2D>> clearTargetPointMap;
    public Point2D mainTargetPosition = null;
    public Point2D agentPosition = null;
    public boolean beforeMove = false;
    public int count = 0;

    public boolean passable(Road road) {
        EntityID roadID = road.getID();
        if(!this.passableEdgeMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        return this.clearTargetPointMap.get(roadID).isEmpty();
    }


    public void analysisRoad(Road road) {
        EntityID roadID = road.getID();
        if(this.passableEdgeMap.containsKey(roadID)) {
            return;
        }
        List<Edge> edges = road.getEdges();
        List<Edge> passableEdge = new ArrayList<>();
        List<Point2D> passablePoint = new ArrayList<>();
        List<Point2D> allEdgePoint = new ArrayList<>();
        for(Edge edge : edges) {
            Point2D point = new Point2D(((edge.getStartX() + edge.getEndX()) / 2), ((edge.getStartY() + edge.getEndY()) / 2));
            allEdgePoint.add(point);
            if(edge.isPassable()) {
                passableEdge.add(edge);
                passablePoint.add(point);
            }
        }
        boolean clear = false;
        for(StandardEntity entity : this.getWorld().getEntitiesOfType(StandardEntityURN.BLOCKADE)) {
            Blockade blockade = (Blockade)entity;
            if(roadID.equals(blockade.getPosition())) {
                clear = true;
            }
        }
        List<Point2D> clearTargetPoint = clear ? new ArrayList<>(passablePoint) : new ArrayList<>();
        this.passableEdgeMap.put(roadID, passableEdge);
        this.passablePointMap.put(roadID, passablePoint);
        this.allEdgePointMap.put(roadID, allEdgePoint);
        this.clearTargetPointMap.put(roadID, clearTargetPoint);
    }

    public List<Point2D> getClearTargetPoint(Road road) {
        EntityID roadID = road.getID();
        if(!this.passableEdgeMap.containsKey(roadID)) {
            this.analysisRoad(road);
        }
        return this.clearTargetPointMap.get(roadID);
    }

    public void removeTargetPoint(Road road, Point2D point) {
        EntityID roadID = road.getID();
        List<Point2D> clearTargetPoint = this.clearTargetPointMap.get(roadID);
        clearTargetPoint.remove(point);
        this.clearTargetPointMap.put(roadID, clearTargetPoint);
    }

    public boolean canStraightForward(Point2D position, Point2D targetPosition, Collection<Edge> edges) {
        for (Edge edge : edges) {
            if (this.linesIntersect(position, targetPosition, edge)) {
                return false;
            }
        }
        return true;
    }

    public boolean linesIntersect(Point2D position, Point2D targetPosition, Edge edge) {
        Point2D start = edge.getStart();
        double startX = start.getX();
        double startY = start.getY();
        Point2D end = edge.getEnd();
        double endX = end.getX();
        double endY = end.getY();
        return java.awt.geom.Line2D.linesIntersect(position.getX(), position.getY(), targetPosition.getX(), targetPosition.getY(), startX, startY, endX, endY) && !this.equalsPoint(targetPosition, ((startX + endX) / 2.0D), (startY + endX) / 2.0D);
    }

    public Point2D getEdgePoint(Edge edge) {
        Point2D start = edge.getStart();
        Point2D end = edge.getEnd();
        return new Point2D(((start.getX() + end.getX()) / 2.0D), ((start.getY() + end.getY()) / 2.0D));
    }

    public boolean equalsPoint(Point2D point, double x, double y) {
        return ((point.getX() == x) && (point.getY() == y));
    }

    //public boolean isClearTime;

    //public Point2D agentPosition;

    //public Road targetRoad;
    //public List<Point2D> targetPoints;


    /*public List<Point2D> getClearPointList(Blockade blockade) {
        return this.getClearPointList(this.getPassableEdge(blockade));
    }

    public List<Point2D> getClearPointList(Road road) {
        return this.getClearPointList(this.getPassableEdge(road));
    }

    public List<Point2D> getClearPointList(List<Edge> edges) {
        int size = edges.size();
        List<Point2D> points = new ArrayList<>(size);
        for (Edge edge : edges) {
            points.add(new Point2D(((edge.getStartX() + edge.getEndX()) / 2), ((edge.getStartY() + edge.getEndY()) / 2)));
        }
        return points;
    }*/


    /*public List<Edge> getPassableEdge(Blockade blockade) {
        EntityID roadID = blockade.getPosition();
        List<Edge> edges = this.passableEdgeMap.get(roadID);
        return edges != null ? edges : this.getPassableEdge((Road) this.getWorld().getEntity(roadID));
    }

    //Area.getNeighbours()
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
    }*/


    /*public List<Point2D> getClearPath(Point2D position, Road road) {
        List<Point2D> clearPath = new ArrayList<>(); //nullで移動
        //List<Point2D> targets = this.getClearPointList(road);

        List<Edge> edges = road.getEdges();
        for(Edge edge : edges) {
            if(edge.isPassable()) {
                Point2D targetPosition = new Point2D(((edge.getStartX() + edge.getEndX()) / 2), ((edge.getStartY() + edge.getEndY()) / 2));
                if(this.linesIntersect(position, targetPosition, edges)) {
                    clearPath.add(targetPosition);
                }
            }
        }

        return clearPath;
    }*/

    /*public Action getTargetAction(AmbulanceTeam agent, Road road, Point2D targetPoint) {
        return this.getTargetAction(agent, new Point2D(agent.getX(), agent.getY()), road, targetPoint);
    }

    public Action getTargetAction(AmbulanceTeam agent, Point2D currentPoint, Road road, Point2D targetPoint) {
        if(agent.getPosition().equals(road.getID())) {
            return new ActionRest(this, this.getCurrentTime());
        }
        return new ActionRest(this, this.getCurrentTime());
    }*/
}
