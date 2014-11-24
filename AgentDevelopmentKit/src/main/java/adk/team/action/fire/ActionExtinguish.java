package adk.team.action.fire;

import adk.team.action.ActionTarget;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.messages.AKExtinguish;
import rescuecore2.worldmodel.EntityID;

public class ActionExtinguish extends ActionTarget {
    
    private int power;
    
    public ActionExtinguish(FireBrigadeTactics tactics, int actionTime, EntityID targetID, int maxPower) {
        super((Tactics)tactics, actionTime, targetID);
        this.power = maxPower;
    }
    
    public ActionExtinguish(FireBrigadeTactics tactics, int actionTime, Building building, int maxPower) {
        this((Tactics)tactics, actionTime, building.getID(), maxPower);
    }
    
    @Override
    public Message getMessage() {
        return new AKExtinguish(this.agentID, this.time, this.target, this.power);
    }
}