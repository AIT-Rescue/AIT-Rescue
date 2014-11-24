package adk.team.action;

import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKRest;

public class ActionRest extends Action {

    public ActionRest(Tactics tactics, int actionTime) {
        super(tactics, actionTime);
    }

    @Override
    public Message getMessage() {
        return new AKRest(this.agentID, this.time);
    }
}