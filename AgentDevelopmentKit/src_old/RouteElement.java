package adk.team.util.graph;

import rescuecore2.worldmodel.EntityID;

import java.util.Set;

public interface RouteElement {

    //Nodeの場合は，Edgeを介して隣接しているNodeのID
    //Edgeの場合は，EdgeにつながっているNodeのID
    public boolean isNeighbourNode(EntityID nodeID);

    //Nodeの場合は，Edgeを介して隣接しているNodeのIDのSet
    //Edgeの場合は，EdgeにつながっているNodeのIDのSet
    public Set<EntityID> getNeighbours();

    //通れるかどうか
    public boolean passable();
}
