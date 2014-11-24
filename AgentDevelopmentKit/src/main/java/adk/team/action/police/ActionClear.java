package adk.team.action.police;

import adk.team.action.ActionTarget;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.messages.AKClear;
import rescuecore2.standard.messages.AKClearArea;
import rescuecore2.worldmodel.EntityID;

public class ActionClear extends ActionTarget {

    private boolean usePosition;
    private int posX;
    private int posY;


    public ActionClear(PoliceForceTactics tactics, int actionTime, EntityID targetID) {
        super((Tactics)tactics, actionTime, targetID);
        this.usePosition = false;
    }

    public ActionClear(PoliceForceTactics tactics, int actionTime, Blockade blockade) {
        this((Tactics) tactics, actionTime, blockade.getID());
    }

    public ActionClear(PoliceForceTactics tactics, int actionTime, EntityID targetID, int destX, int destY) {
        super((Tactics)tactics, actionTime, targetID);
        this.usePosition = true;
        this.posX = destX;
        this.posY = destY;
    }

    public ActionClear(PoliceForceTactics tactics, int actionTime, Blockade blockade, int destX, int destY) {
        this((Tactics)tactics, actionTime, blockade.getID(), destX, destY);
    }

    @Override
    public Message getMessage() {
        return this.usePosition ? new AKClear(this.agentID, this.time, this.target) : new AKClearArea(this.agentID, this.time, this.posX, this.posY);
    }
}
