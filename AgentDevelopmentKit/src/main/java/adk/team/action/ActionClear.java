package adk.team.action;

import adk.team.action.ActionTarget;
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


    public ActionClear(TacticsPolice tactics, int actionTime, EntityID targetID) {
        super(tactics, actionTime, targetID);
        this.usePosition = false;
    }

    public ActionClear(TacticsPolice tactics, int actionTime, Blockade blockade) {
        this(tactics, actionTime, blockade.getID());
    }

    public ActionClear(TacticsPolice tactics, int actionTime, EntityID targetID, int destX, int destY) {
        super(tactics, actionTime, targetID);
        this.usePosition = true;
        this.posX = destX;
        this.posY = destY;
    }

    public ActionClear(TacticsPolice tactics, int actionTime, Blockade blockade, int destX, int destY) {
        this(tactics, actionTime, blockade.getID(), destX, destY);
    }

    @Override
    public Message getCommand() {
        return this.usePosition ? new AKClear(this.agentID, this.time, this.target) : new AKClearArea(this.agentID, this.time, this.posX, this.posY);
    }
}
