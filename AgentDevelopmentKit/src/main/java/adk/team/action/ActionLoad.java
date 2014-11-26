package adk.team.action;

import adk.team.action.ActionTarget;
import adk.team.tactics.TacticsAmbulance;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.messages.AKLoad;
import rescuecore2.worldmodel.EntityID;

public class ActionLoad extends ActionTarget {

    public ActionLoad(TacticsAmbulance tactics, int actionTime, EntityID targetID) {
        super(tactics, actionTime, targetID);
    }

    public ActionLoad(TacticsAmbulance tactics, int actionTime, Civilian civilian) {
        super(tactics, actionTime, civilian.getID());
    }

    @Override
    public Message getCommand() {
        return new AKLoad(this.agentID, this.time, this.target);
    }
}
