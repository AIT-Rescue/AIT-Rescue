package adk.team.action;

import rescuecore2.worldmodel.EntityID;

public abstract class ActionTarget extends Action {

    protected EntityID target;

    public ActionTarget(Tactics tactics, int actionTime, EntityID targetID) {
        super(tactics, actionTime);
        this.target = targetID;
    }

    public EntityID getTarget() {
        return this.target;
    }
}
