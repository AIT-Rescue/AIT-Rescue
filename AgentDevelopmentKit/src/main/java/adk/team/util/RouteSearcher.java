package adk.team.util;

import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Human;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public interface RouteSearcher {
    
    List<EntityID> noTargetMove(int time, EntityID from);

    default List<EntityID> noTargetMove(int time, Human from) {
        return this.noTargetMove(time, from.getPosition());
    }
    
    List<EntityID> getPath(int time, EntityID from, EntityID to);
    
    default List<EntityID> getPath(int time, Human from, EntityID to) {
        return this.getPath(time, from.getPosition(), to);
    }
    
    default List<EntityID> getPath(int time, EntityID from, Area to) {
        return this.getPath(time, from, to.getID());
    }
    
    default List<EntityID> getPath(int time, Human from, Area to) {
        return this.getPath(time, from.getPosition(), to.getID());
    }
    
    default List<EntityID> getPath(int time, EntityID from, Blockade blockade) {
        return this.getPath(time, from, blockade.getPosition());
    }
    
    default List<EntityID> getPath(int time, Human from, Blockade blockade) {
        return this.getPath(time, from.getPosition(), blockade.getPosition());
    }
}