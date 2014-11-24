package adk.team.action;

import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKMove;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class ActionMove extends Action {
    
    private List<EntityID> path;

    private boolean usePosition;
    private int posX;
    private int posY;
    
    public ActionMove(Tactics tactics, int actionTime, List<EntityID> movePath) {
        super(tactics, actionTime);
        this.usePosition = false;
        this.path = movePath;
    }
    
    public ActionMove(Tactics tactics, int actionTime, List<EntityID> movePath, int destX, int destY) {
        super(tactics, actionTime);
        this.usePosition = true;
        this.path = movePath;
        this.posX = destX;
        this.posY = destY;
    }
    
    @Override
    public Message getMessage() {
        return this.usePosition ? new AKMove(this.agentID, this.time, this.path, this.posX, this.posY) : new AKMove(this.agentID, this.time, this.path);
    }
}