package adk.team.action;

public abstract class Action {
    
    protected int time;
    
    protected EntityID agentID;
    
    public Action(Tactics tactics, int actionTime) {
        this.time = actionTime;
        this.agentID = tactics.getID();
    }
    
    public abstract Message getMessage();
    
    public int getTime() {
        return this.time;
    }
    
    public EntityID getID() {
        return this.agentID;
    }
}