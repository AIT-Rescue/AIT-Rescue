package adk.launcher.agent;

import adk.team.control.ControlAmbulance;
import rescuecore2.standard.entities.AmbulanceCentre;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class AmbulanceTeamStation extends ControllingStation<AmbulanceCentre> {

    private ControlAmbulance ca;

    public AmbulanceTeamStation(ControlAmbulance controlAmbulance) {
        super(controlAmbulance);
        this.ca = controlAmbulance;
    }

    @Override
    protected void setCenterUniqueValue() {

    }

    @Override
    protected void setCenterEntity() {
        this.ca.me = this.me();
    }

    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.AMBULANCE_CENTRE);
    }

    @Override
    public String toString() {
        return "Ambulance Centre [control: " + this.ca.getControlName() + " ]";
    }

    @Override
    protected AmbulanceCentre me() {
        try {
            return (AmbulanceCentre)this.model.getEntity(this.getID());
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
