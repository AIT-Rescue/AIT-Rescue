package adk.team.action;

import adk.team.tactics.Tactics;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKRest;

public class ActionRest extends Action {

    public ActionRest(Tactics tactics, int actionTime) {
        super(tactics, actionTime);
    }

    @Override
    public Message getCommand() {
        return new AKRest(this.agentID, this.time);
    }
}