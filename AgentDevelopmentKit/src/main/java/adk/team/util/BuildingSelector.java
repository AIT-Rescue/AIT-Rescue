package adk.team.util;

import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.EntityID;

public interface BuildingSelector extends TargetSelector {

    public void add(Building building);

    public void remove(Building building);
}