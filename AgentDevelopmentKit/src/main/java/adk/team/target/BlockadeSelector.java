package comlib.adk.util.target;

import comlib.adk.team.tactics.Tactics;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.worldmodel.EntityID;

public abstract class BlockadeSelector extends TargetSelector {

    public BlockadeSelector(Tactics user) {
        super(user);
    }

    public abstract void add(Blockade blockade);

    public abstract void add(EntityID id);

    public abstract void remove(Blockade blockade);

    public abstract void remove(EntityID id);
}
