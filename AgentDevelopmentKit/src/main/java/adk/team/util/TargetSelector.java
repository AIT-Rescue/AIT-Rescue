package adk.team.util;

import rescuecore2.worldmodel.EntityID;

public interface TargetSelector {

    public void add(EntityID id);

    public void remove(EntityID id);

    public EntityID getTarget(int time);
}