package adk.team.util;

import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Road;

public interface DebrisRemovalSelector extends TargetSelector {

    public void add(Road road);

    public void add(Blockade blockade);

    public void remove(Road road);

    public void remove(Blockade blockade);
}
