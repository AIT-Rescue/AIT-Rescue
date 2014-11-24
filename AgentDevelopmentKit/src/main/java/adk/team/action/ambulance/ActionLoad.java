package adk.team.action.ambulance;

import adk.team.action.ActionTarget;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.messages.AKLoad;
import rescuecore2.worldmodel.EntityID;

public class ActionLoad extends ActionTarget {

    public ActionLoad(AmbulanceTeamTactics tactics, int actionTime, EntityID targetID) {
        super((Tactics)tactics, actionTime, targetID);
    }

    public ActionLoad(AmbulanceTeamTactics tactics, int actionTime, Civilian civilian) {
        super((Tactics)tactics, actionTime, civilian.getID());
    }

    @Override
    public Message getMessage() {
        return new AKLoad(this.agentID, this.time, this.target);
    }
}
