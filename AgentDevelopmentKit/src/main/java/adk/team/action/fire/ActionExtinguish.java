package adk.team.action.fire;

public class ActionExtinguish extends ActionRescue {
    
    private int power;
    
    public ActionExtinguish(Tactics tactics, int actionTime, EntityID building, int maxPower) {
        super(tactics, actionTime, building);
        this.power = maxPower;
    }
    
    public ActionExtinguish(Tactics tactics, int actionTime, Building building, int maxPower) {
        this(tactics, actionTime, building.getID(), maxPower);
    }
    
    @Override
    public Message getMessage() {
        return new AKExtinguish(this.agentID, this.time, this.target, this.power);
    }
}