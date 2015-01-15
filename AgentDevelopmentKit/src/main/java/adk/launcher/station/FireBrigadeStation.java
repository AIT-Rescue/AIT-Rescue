package adk.launcher.station;

import adk.team.control.ControlFire;
import rescuecore2.standard.entities.FireStation;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class FireBrigadeStation extends ControllingStation<FireStation> {
    private ControlFire cf;

    public FireBrigadeStation(ControlFire controlFire, boolean pre) {
        super(controlFire, pre);
        this.cf = controlFire;
    }

    @Override
    protected void setCenterUniqueValue() {

    }

    @Override
    protected void setCenterEntity() {
        this.cf.me = this.me();
    }

    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.FIRE_STATION);
    }


    @Override
    public String toString() {
        return "Fire Station [control: " + this.cf.getControlName() + " ]";
    }

    @Override
    protected FireStation me() {
        try {
            return (FireStation)this.model.getEntity(this.getID());
        }
        catch(NullPointerException e) {
            return null;
        }
    }

    @Override
    protected StandardEntity location() {
        try {
            return this.model.getEntity(this.getID());
        }
        catch(NullPointerException e) {
            return null;
        }
    }
}
