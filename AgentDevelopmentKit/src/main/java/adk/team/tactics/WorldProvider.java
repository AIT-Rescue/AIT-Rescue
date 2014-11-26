package adk.team.tactics;

//import com.google.common.base.Function;
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
        /*
        List<StandardEntity> entityList = new ArrayList<>(this.getWorld().getEntitiesOfType(StandardEntityURN.REFUGE));
        List<Refuge> refugeList = Lists.transform(entityList, new Function<StandardEntity, Refuge>() {
            @Override
            public Refuge apply(StandardEntity input) {
                return (Refuge)input;
            }
        });
        */
        return this.getWorld().getEntitiesOfType(StandardEntityURN.REFUGE).stream().map(entity -> (Refuge) entity).collect(Collectors.toList());
    }

    /*
    default <T extends StandardEntity> List<T> getEntityList(StandardEntityURN urn) {
        return this.getWorld().getEntitiesOfType(urn).stream().map(entity -> (T)entity).collect(Collectors.toList());
    }
    */

    default EntityID getID() {
        return this.getAgentID();
    }
}
