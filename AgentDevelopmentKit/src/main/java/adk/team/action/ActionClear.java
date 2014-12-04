package adk.team.action;

import adk.team.tactics.TacticsPolice;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.messages.AKClear;
import rescuecore2.standard.messages.AKClearArea;
import rescuecore2.worldmodel.EntityID;

public class ActionClear extends ActionTarget {

    private boolean usePosition;
    private int posX;
    private int posY;

    public ActionClear(TacticsPolice tactics, EntityID targetID) {
        super(tactics, targetID);
        this.usePosition = false;
    }

    public ActionClear(TacticsPolice tactics, Blockade blockade) {
        this(tactics, blockade.getID());
    }

    public ActionClear(TacticsPolice tactics, int destX, int destY) {
        super(tactics, null);
        this.usePosition = true;
        this.posX = destX;
        this.posY = destY;
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
        return this.usePosition ? new AKClearArea(agentID, time, this.posX, this.posY) : new AKClear(agentID, time, this.target);
    }
}
