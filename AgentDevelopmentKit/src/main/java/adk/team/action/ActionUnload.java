package adk.team.action;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKUnload;
import rescuecore2.worldmodel.EntityID;

public class ActionUnload extends Action<TacticsAmbulance> {

    public ActionUnload(TacticsAmbulance tactics) {
        super(tactics);
    }

    @Override
    public Message getCommand(EntityID agentID, int time) {
        return new AKUnload(agentID, time);
    }
}
