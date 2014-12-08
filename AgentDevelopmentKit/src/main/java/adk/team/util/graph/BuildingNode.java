package adk.team.util.graph;

import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuildingNode extends RouteNode{

    private EntityID buildingID;

    private RouteNode[] neighbourNode;

    //road > road > ... > Road > Buildingむしろ逆か
    private List<EntityID> path;

    public BuildingNode(EntityID id) {
        this.buildingID = id;
    }

    @Override
    public EntityID getNodeID() {
        return this.buildingID;
    }

    @Override
    public List<EntityID> getPath(RouteNode from, EntityID target) {
        if(target.getValue() == this.buildingID.getValue()) {
            List<EntityID> result = new ArrayList<>(this.path);
            Collections.reverse(result);
            return result;
        }
        return null;
    }
}
