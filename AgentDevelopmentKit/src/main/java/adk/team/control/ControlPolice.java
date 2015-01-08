package adk.team.control;

import rescuecore2.standard.entities.PoliceOffice;
import rescuecore2.standard.entities.StandardEntity;

public abstract class ControlPolice extends Control<PoliceOffice> {

    public PoliceOffice me;

    @Override
    public PoliceOffice getOwner() {
        return this.me;
    }

    @Override
    public PoliceOffice me() {
        return this.me;
    }

    @Override
    public StandardEntity getOwnerLocation() {
        return this.me;
    }
}
