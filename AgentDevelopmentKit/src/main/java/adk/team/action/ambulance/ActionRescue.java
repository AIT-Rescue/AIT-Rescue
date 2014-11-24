package adk.team.action.ambulance;

import adk.team.action.ActionTarget;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.messages.AKRescue;
import rescuecore2.worldmodel.EntityID;

public class ActionRescue extends ActionTarget {


    public ActionRescue(AmbulanceTeamTactics tactics, int actionTime, EntityID targetID) {
        super((Tactics)tactics, actionTime, targetID);
    }

    public ActionRescue(AmbulanceTeamTactics tactics, int actionTime, Human human) {
        this((Tactics)tactics, actionTime, human.getID());
    }

    @Override
    public Message getMessage() {
        return new AKRescue(this.agentID, this.time, this.target);
    }
}
