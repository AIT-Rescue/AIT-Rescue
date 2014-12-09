package adk.team.util.graph;

import rescuecore2.worldmodel.EntityID;

import java.util.List;

public interface GraphElement {

    public boolean addPath(List<EntityID> path);
}
