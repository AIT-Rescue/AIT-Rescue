package comlib.adk.util.target;

import comlib.adk.team.tactics.Tactics;
import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.EntityID;

public abstract class BuildingSelector extends TargetSelector {

    public BuildingSelector(Tactics user) {
        super(user);
    }

    public abstract void add(Building building);

    public abstract void add(EntityID id);

    public abstract void remove(Building building);

    public abstract void remove(EntityID id);
}