package adk.team.action;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.messages.AKRescue;
import rescuecore2.worldmodel.EntityID;

public class ActionRescue extends ActionTarget {


    public ActionRescue(TacticsAmbulance tactics, EntityID targetID) {
        super(tactics, targetID);
    }

    public ActionRescue(TacticsAmbulance tactics, Human human) {
        this(tactics, human.getID());
    }

    @Override
    public Message getCommand(EntityID agentID, int time) {
        return new AKRescue(agentID, time, this.target);
    }
}
