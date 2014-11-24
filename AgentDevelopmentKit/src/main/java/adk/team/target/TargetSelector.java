package comlib.adk.util.target;

import comlib.adk.team.tactics.Tactics;
import rescuecore2.worldmodel.EntityID;

public abstract class TargetSelector {

    protected Tactics tactics;

    public TargetSelector(Tactics t) {
        this.tactics = t;
    }

    public abstract EntityID getTarget(int time);
}