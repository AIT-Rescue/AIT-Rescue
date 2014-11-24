package adk.team.action;

public class ActionMove extends Action {
    
    private boolean usePos;
    
    private List<EntityID> path;
    
    private int posX;
    private int posY;
    
    public ActionMove(Tactics tactics, int actionTime, List<EntityID> movePath) {
        super(tactics, actionTime);
        this.usePos = false;
        this.path = movePath;
    }
    
    public ActionMove(Tactics tactics, int actionTime, List<EntityID> movePath, int destX, int destY) {
        super(tactics, actionTime);
        this.usePos = true;
        this.path = movePath;
        this.posX = destX;
        this.posY = destY;
    }
    
    @Override
    public Message getMessage() {
        return usePos ? new AKMove(this.agentID, this.time, this.path, this.posX, this.posY) : new AKMove(this.agentID, this.time, this.path);
    }
}