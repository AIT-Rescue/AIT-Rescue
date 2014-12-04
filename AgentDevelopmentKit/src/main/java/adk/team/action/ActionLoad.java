package adk.team.action;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.messages.AKLoad;
import rescuecore2.worldmodel.EntityID;

public class ActionLoad extends ActionTarget {

    public ActionLoad(TacticsAmbulance tactics, EntityID targetID) {
        super(tactics, targetID);
    }

    public ActionLoad(TacticsAmbulance tactics, Civilian civilian) {
        super(tactics, civilian.getID());
    }

    @Override
    public Message getCommand(EntityID agentID, int time) {
        return new AKLoad(agentID, time, this.target);
    }
}
