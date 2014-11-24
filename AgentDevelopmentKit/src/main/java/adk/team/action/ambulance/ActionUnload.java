package adk.team.action.ambulance;

import adk.team.action.Action;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKUnload;

public class ActionUnload extends Action {

    public ActionUnload(AmbulanceTeamTactics tactics, int actionTime) {
        super((Tactics)tactics, actionTime);
    }

    @Override
    public Message getMessage() {
        return new AKUnload(this.agentID, this.time);
    }
}
