package adk.team.util;

import rescuecore2.standard.entities.Blockade;
import rescuecore2.worldmodel.EntityID;

public interface BlockadeSelector extends TargetSelector {

    public void add(Blockade blockade);

    public void remove(Blockade blockade);
}
