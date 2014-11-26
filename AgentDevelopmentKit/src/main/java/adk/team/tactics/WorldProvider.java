package adk.team.tactics;

//import com.google.common.collect.Lists;

import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface WorldProvider<E extends StandardEntity> {

    public EntityID getAgentID();

    public StandardWorldModel getWorld();

    public E getAgent();

    default E me() {
        //return (E)this.getWorld().getEntity(this.getAgentID());
        return this.getAgent();
    }

    default StandardEntity location() {
        StandardEntity entity = this.me();
        return entity instanceof Human ?((Human)entity).getPosition(this.getWorld()):entity;
    }

    default List<Refuge> getRefuges() {
        //Collection<StandardEntity> -> List<StandardEntity>
        List<StandardEntity> entityList = new ArrayList<>(this.getWorld().getEntitiesOfType(StandardEntityURN.REFUGE));
        //Lists.
        //List<StandardEntity> -> List<Refuge>
        return this.getWorld().getEntitiesOfType(StandardEntityURN.REFUGE).stream().map(entity -> (Refuge) entity).collect(Collectors.toList());
    }

    default EntityID getID() {
        return this.getAgentID();
    }
}
