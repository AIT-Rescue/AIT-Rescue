package adk.team.util;

import rescuecore2.standard.entities.Blockade;

public interface BlockadeSelector extends TargetSelector {

    public void add(Blockade blockade);

    public void remove(Blockade blockade);
}
