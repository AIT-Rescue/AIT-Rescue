package adk.team.action;

public class ActionRest extends Action {
    
    public ActionRest(Tactics tactics, int actionTime) {
        super(tactics, actionTime);
    }
    
    @Override
    public Message getMessage() {
        return new AKRest(this.agentID, this.time);
    }