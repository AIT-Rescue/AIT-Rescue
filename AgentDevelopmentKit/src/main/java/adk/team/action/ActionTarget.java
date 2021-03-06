package adk.team.action;

import adk.team.tactics.Tactics;
import rescuecore2.worldmodel.EntityID;

public abstract class ActionTarget extends Action {

    protected EntityID target;

    public ActionTarget(Tactics tactics, EntityID targetID) {
        super(tactics);
        this.target = targetID;
    }

    public EntityID getTarget() {
        return this.target;
    }
}
