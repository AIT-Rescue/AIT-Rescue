package adk.team.action;

import adk.team.tactics.Tactics;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKRest;
import rescuecore2.worldmodel.EntityID;

public class ActionRest extends Action {

    public ActionRest(Tactics tactics) {
        super(tactics);
    }

    @Override
    public Message getCommand(EntityID agentID, int time) {
        return new AKRest(agentID, time);
    }
}