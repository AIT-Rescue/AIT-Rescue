package adk.team.util.provider;


import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public interface WorldProvider<E extends StandardEntity> {

    public int getCurrentTime();

    public ChangeSet getUpdateWorldData();

    public StandardWorldModel getWorld();

    public EntityID getOwnerID();

    public E getOwner();

    public StandardEntity getOwnerLocation();

    public List<Refuge> getRefugeList();

    default E me() {
        return this.getOwner();
    }

    default StandardEntity location() {
        return this.getOwnerLocation();
    }

    default List<Refuge> getRefuges() {
        return this.getRefugeList();
    }

    default EntityID getID() {
        return this.getOwnerID();
    }
}
