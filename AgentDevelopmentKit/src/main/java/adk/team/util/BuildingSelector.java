package adk.team.util;

import rescuecore2.standard.entities.Building;

public interface BuildingSelector extends TargetSelector {

    public void add(Building building);

    public void remove(Building building);
}