package adk.team.action;

import adk.team.action.Action;
import adk.team.tactics.TacticsAmbulance;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKUnload;

public class ActionUnload extends Action {

    public ActionUnload(TacticsAmbulance tactics, int actionTime) {
        super(tactics, actionTime);
    }

    @Override
    public Message getCommand() {
        return new AKUnload(this.agentID, this.time);
    }
}
