package adk.team.util.provider;

import comlib.message.information.*;
import rescuecore2.standard.entities.*;
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
        return getOwner();
    }

    default StandardEntity location() {
        return getOwnerLocation();
    }

    default List<Refuge> getRefuges() {
        return getRefugeList();
    }

    default EntityID getID() {
        return getOwnerID();
    }
}
