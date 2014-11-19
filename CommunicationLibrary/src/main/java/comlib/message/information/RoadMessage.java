package comlib.message.information;

import comlib.message.MapMessage;
import comlib.message.MessageID;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.worldmodel.EntityID;

public class RoadMessage extends MapMessage {

    private int rawBlockadeID;
    private int rawHumanPosition;
    private EntityID blockadeID;
    private EntityID humanPosition;
    private int repairCost;
    //private int[] apexes;
    //private int x;
    //private int y;

    public RoadMessage(Blockade blockade) {
        super(MessageID.roadMessage);
        this.blockadeID = blockade.getID();
        this.humanPosition = blockade.getPosition();
        this.repairCost = blockade.getRepairCost();
        //this.apexes = blockade.getApexes();
        //this.x = blockade.getX();
        //this.y = blockade.getY();

    }

    public RoadMessage(int time, int ttl, int id, int position, int cost) {
        super(MessageID.roadMessage, time, ttl);
        this.rawBlockadeID = id;
        this.rawHumanPosition = position;
        this.repairCost = cost;
    }

    public EntityID getID() {
        if(this.blockadeID == null) {
            this.blockadeID = new EntityID(this.rawBlockadeID);
        }
        return this.blockadeID;
    }

    public EntityID getPosition() {
        if(this.humanPosition == null) {
            this.humanPosition = new EntityID(this.rawHumanPosition);
        }
        return this.humanPosition;
    }

    public int getRepairCost() {
        return this.repairCost;
    }

    /*
    public int[] getApexes() {
        return this.apexes;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    */
}
