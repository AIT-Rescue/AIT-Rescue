package adk.team.util.graph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteEdge {
    public final List<EntityID> element;
    //element.get(0);
    public final EntityID firstNodeID;
    //element.get(element.size() - 1);
    public final EntityID secondNodeID;
    //cache
    protected List<EntityID> fromFirst;
    protected List<EntityID> fromSecond;

    public Set<EntityID> impassableArea;

    protected Table<EntityID, EntityID, Double> routeDistance;
    protected Map<EntityID, Double> roadDistance;

    private RouteEdge(StandardWorldModel world, List<EntityID> path) {
        this.firstNodeID = path.get(0);
        this.secondNodeID = path.get(path.size() - 1);
        this.element = path;
        this.impassableArea = new HashSet<>();
        this.initRoadDistance(world);
        this.createCache();
        this.initDistanceToSecond();
    }

    private RouteEdge(RouteEdge original) {
        this.firstNodeID = original.firstNodeID;
        this.secondNodeID = original.secondNodeID;
        this.element = original.element;
        this.impassableArea = new HashSet<>(original.impassableArea);
        this.routeDistance = HashBasedTable.create(original.routeDistance);
        this.roadDistance = new HashMap<>(original.roadDistance);
        this.fromFirst = original.getPath(this.firstNodeID);
        this.fromSecond = original.getPath(this.secondNodeID);
    }

    public static RouteEdge getInstance(StandardWorldModel world, List<EntityID> path) {
        if(world != null && path != null && path.size() > 1) {
            return new RouteEdge(world, path);
        }
        return null;
    }

    public static RouteEdge copy(RouteEdge original) {
        return original != null ? new RouteEdge(original) : null;
    }

    public boolean isNeighbourNode(RouteNode node) {
        return this.isNeighbourNode(node.nodeID);
    }

    public boolean isNeighbourNode(EntityID nodeID) {
        return this.isFirstNode(nodeID) || this.isSecondNode(nodeID);
    }

    public boolean isFirstNode(RouteNode node) {
        return this.isFirstNode(node.nodeID);
    }

    public boolean isFirstNode(EntityID nodeID) {
        return this.firstNodeID.getValue() == nodeID.getValue();
    }

    public boolean isSecondNode(RouteNode node) {
        return this.isSecondNode(node.nodeID);
    }

    public boolean isSecondNode(EntityID nodeID) {
        return this.secondNodeID.getValue() == nodeID.getValue();
    }

    public boolean isEdgeElement(EntityID areaID) {
        return this.element.contains(areaID);
    }

    public EntityID getAnotherNodeID(EntityID nodeID) {
        int value = nodeID.getValue();
        if(this.firstNodeID.getValue() == value) {
            return this.secondNodeID;
        }
        if(this.secondNodeID.getValue() == value) {
            return this.firstNodeID;
        }
        return null;
    }

    public boolean passable() {
        return this.impassableArea.isEmpty();
    }

    public RouteEdge setPassable(EntityID areaID, boolean flag) {
        if(this.isEdgeElement(areaID)) {
            if (flag) {
                this.impassableArea.add(areaID);
            } else {
                this.impassableArea.remove(areaID);
            }
        }
        return this;
    }

    public double getDistance() {
        return this.getDistance(this.firstNodeID, this.secondNodeID);
    }

    public double getRoadDistance(EntityID areaID) {
        return this.roadDistance.get(areaID);
    }

    public double getDistance(EntityID areaID, EntityID targetID) {
        if(areaID.getValue() == targetID.getValue()) {
            return 0.0D;
        }
        int areaIndex = this.element.indexOf(areaID);
        int targetIndex = this.element.indexOf(targetID);
        if(areaIndex >= 0 && targetIndex >= 0) {
            boolean reverse = areaIndex > targetIndex;
            double start = this.routeDistance.get(reverse ? targetID : areaID, this.secondNodeID);
            double end = this.routeDistance.get(reverse ? areaID : targetID, this.secondNodeID);
            double road = this.roadDistance.get(reverse ? areaID : targetID);
            double areaDistance = this.isNeighbourNode(areaID) ? this.roadDistance.get(areaID) : (this.roadDistance.get(areaID) / 2);
            double targetDistance = this.isNeighbourNode(targetID) ? this.roadDistance.get(targetID) : (this.roadDistance.get(targetID) / 2);
            return Math.abs(start - end - road + areaDistance + targetDistance);
        }
        return Double.NaN;
    }

    public List<EntityID> getPath(RouteNode from) {
        return this.getPath(from.nodeID);
    }

    public List<EntityID> getPath(EntityID from) {
        if(this.isFirstNode(from)) {
            return new ArrayList<>(this.fromFirst);
        }
        if(this.isSecondNode(from)) {
            return new ArrayList<>(this.fromSecond);
        }
        return null;
    }

    public List<EntityID> getPath(RouteNode from, EntityID to) {
        return this.getPath(from.nodeID, to);
    }

    public List<EntityID> getPath(EntityID from, EntityID to) {
        int nodeIndex = this.isFirstNode(from) ? 1 : this.isSecondNode(from) ? this.element.size() - 2 : this.element.indexOf(from);
        int targetIndex = this.isSecondNode(to) ? this.element.size() - 2 : this.isFirstNode(from) ? 1 : this.element.indexOf(to);
        if(nodeIndex != -1 && targetIndex != -1) {
            if(nodeIndex > targetIndex) {
                List<EntityID> path = Arrays.asList(Arrays.copyOfRange(this.element.toArray(new EntityID[this.element.size()]), targetIndex, nodeIndex + 1));
                Collections.reverse(path);
                return path;
            }
            return Arrays.asList(Arrays.copyOfRange(this.element.toArray(new EntityID[this.element.size()]), nodeIndex, targetIndex + 1));
        }
        return null;
    }

    private void initRoadDistance(StandardWorldModel world) {
        this.roadDistance = new HashMap<>();
        int limit = this.element.size() - 1;
        Area area = (Area)world.getEntity(this.firstNodeID);
        this.roadDistance.put(this.firstNodeID, PositionUtil.getDistance(area.getLocation(world), area.getEdgeTo(this.element.get(1))));
        area = (Area)world.getEntity(this.secondNodeID);
        this.roadDistance.put(this.secondNodeID, PositionUtil.getDistance(area.getLocation(world), area.getEdgeTo(this.element.get(limit - 1))));
        //this.roadDistance.put(this.firstNodeID, 0.0D);
        //this.roadDistance.put(this.secondNodeID, 0.0D);
        for(int i = 1; i < limit; i++) {
            EntityID areaID = this.element.get(i);
            area = (Area)world.getEntity(areaID);
            double distance = PositionUtil.getDistance(area.getEdgeTo(this.element.get(i - 1)), area.getEdgeTo(this.element.get(i + 1)));
            this.roadDistance.put(areaID, distance);
        }
    }

    private void initDistanceToSecond() {
        this.routeDistance = HashBasedTable.create();
        double result = 0.0D;
        for(int i = this.element.size() - 2; i >= 0; i--) {
            EntityID areaID = this.element.get(i);
            this.routeDistance.put(areaID, this.secondNodeID, result);
            this.routeDistance.put(this.secondNodeID, areaID, result);
            result += this.roadDistance.get(areaID);
        }
    }

    private void createCache() {
        List<EntityID> path = new ArrayList<>(this.element);
        path.remove(this.firstNodeID);
        path.remove(this.secondNodeID);
        List<EntityID> reverse = new ArrayList<>(path);
        Collections.reverse(reverse);
        this.fromFirst = path;
        this.fromSecond = reverse;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RouteEdge && this.equals((RouteEdge)o);
    }

    public boolean equals(RouteEdge edge) {
        if(!this.isNeighbourNode(edge.firstNodeID) || !this.isNeighbourNode(edge.secondNodeID)) {
            return false;
        }
        List<EntityID> edgeElement = new ArrayList<>(edge.element);
        if(edgeElement.size() != this.element.size()) {
            return false;
        }
        if(edgeElement.get(0).getValue() != this.element.get(0).getValue()) {
            Collections.reverse(edgeElement);
        }
        for(int i = 0; i < edgeElement.size(); i++) {
            if(edgeElement.get(i).getValue() != this.element.get(i).getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        //Long.hashCode
        return this.firstNodeID.getValue() ^ this.secondNodeID.getValue();
    }

    /*public long getHashKey(EntityID rowKey, EntityID columnKey) {
        return ((long)rowKey.getValue() << 32) + (long)columnKey.getValue();
    }*/

    /*
    public EntityID getFirstNodeID() {
        return this.firstNodeID;
    }

    public EntityID getSecondNodeID() {
        return this.secondNodeID;
    }

    public List<EntityID> getAllElement() {
        return this.element;
    }
    */

     /*
    protected Table<EntityID, EntityID, Double> getDistanceTable() {
        return  this.routeDistance;
    }

    protected Map<EntityID, Double> getDistanceMap() {
        return this.roadDistance;
    }
    */

    /*public List<EntityID> getPath(EntityID nodeID, EntityID target) {
        if(this.isEdgeElement(nodeID) && this.isEdgeElement(target)) {
            List<EntityID> path = new ArrayList<>();
            int start = this.isFirstNode(nodeID) ? 1 : this.isSecondNode(nodeID) ? this.element.size() - 2 : this.element.indexOf(nodeID);
            int end = this.isSecondNode(target) ? this.element.size() - 2 : this.isFirstNode(nodeID) ? 1 : this.element.indexOf(target);
            if(start < end) {
                for(int i = start; i <= end; i++) {
                    path.add(this.element.get(i));
                }
            }
            else {
                for (int i = start; i >= end; i--) {
                    path.add(this.element.get(i));
                }
            }
            return path;
        }
        return null;
    }*/
}
