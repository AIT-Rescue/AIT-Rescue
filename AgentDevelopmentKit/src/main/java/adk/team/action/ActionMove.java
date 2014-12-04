package adk.team.action;

import adk.team.tactics.Tactics;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKMove;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class ActionMove extends Action {
    
    private List<EntityID> path;

    private boolean usePosition;
    private int posX;
    private int posY;
    
    public ActionMove(Tactics tactics, List<EntityID> movePath) {
        super(tactics);
        this.usePosition = false;
        this.path = movePath;
    }
    
    public ActionMove(Tactics tactics, List<EntityID> movePath, int destX, int destY) {
        super(tactics);
        this.usePosition = true;
        this.path = movePath;
        this.posX = destX;
        this.posY = destY;
    }

    public List<EntityID> getPath() {
        return this.path;
    }

    public boolean getUsePosition() {
        return this.usePosition;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }
    
    @Override
    public Message getCommand(EntityID agentID, int time) {
        return this.usePosition ? new AKMove(agentID, time, this.path, this.posX, this.posY) : new AKMove(agentID, time, this.path);
    }
}