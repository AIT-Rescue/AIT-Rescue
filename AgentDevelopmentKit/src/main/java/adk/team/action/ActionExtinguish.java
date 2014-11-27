package adk.team.action;

import adk.team.tactics.TacticsFire;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.messages.AKExtinguish;
import rescuecore2.worldmodel.EntityID;

public class ActionExtinguish extends ActionTarget {
    
    private int power;
    
    public ActionExtinguish(TacticsFire tactics, int actionTime, EntityID targetID, int maxPower) {
        super(tactics, actionTime, targetID);
        this.power = maxPower;
    }
    
    public ActionExtinguish(TacticsFire tactics, int actionTime, Building building, int maxPower) {
        this(tactics, actionTime, building.getID(), maxPower);
    }
    
    @Override
    public Message getCommand() {
        return new AKExtinguish(this.agentID, this.time, this.target, this.power);
    }
}