package adk.team.control;

import rescuecore2.standard.entities.AmbulanceCentre;
import rescuecore2.standard.entities.StandardEntity;

public abstract class ControlAmbulance extends Control<AmbulanceCentre>{

    public AmbulanceCentre me;

    @Override
    public AmbulanceCentre getOwner() {
        return this.me;
    }

    @Override
    public AmbulanceCentre me() {
        return this.me;
    }

    @Override
    public StandardEntity getOwnerLocation() {
        return this.me;
    }
}
