package adk.team.action;

import adk.team.tactics.Tactics;
import rescuecore2.worldmodel.EntityID;

public abstract class ActionTarget<T extends Tactics> extends Action<T> {

    protected EntityID target;

    public ActionTarget(T tactics, EntityID targetID) {
        super(tactics);
        this.target = targetID;
    }

    public EntityID getTarget() {
        return this.target;
    }
}
