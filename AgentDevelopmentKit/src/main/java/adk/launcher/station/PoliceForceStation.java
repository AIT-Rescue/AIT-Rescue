package adk.launcher.station;

import adk.team.control.ControlPolice;
import rescuecore2.standard.entities.PoliceOffice;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class PoliceForceStation extends ControllingStation<PoliceOffice> {
    private ControlPolice cp;

    public PoliceForceStation(ControlPolice controlPolice) {
        super(controlPolice);
        this.cp = controlPolice;
    }

    @Override
    protected void setCenterUniqueValue() {

    }

    @Override
    protected void setCenterEntity() {
        this.cp.me = this.me();
    }

    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.POLICE_OFFICE);
    }

    @Override
    public String toString() {
        return "Police Office [control: " + this.cp.getControlName() + " ]";
    }

    @Override
    protected PoliceOffice me() {
        try {
            return (PoliceOffice)this.model.getEntity(this.getID());
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
