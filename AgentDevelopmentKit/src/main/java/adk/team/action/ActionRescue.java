package adk.team.action;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.messages.AKRescue;
import rescuecore2.worldmodel.EntityID;

public class ActionRescue extends ActionTarget {


    public ActionRescue(TacticsAmbulance tactics, int actionTime, EntityID targetID) {
        super(tactics, actionTime, targetID);
    }

    public ActionRescue(TacticsAmbulance tactics, int actionTime, Human human) {
        this(tactics, actionTime, human.getID());
    }

    @Override
    public Message getCommand() {
        return new AKRescue(this.agentID, this.time, this.target);
    }
}
