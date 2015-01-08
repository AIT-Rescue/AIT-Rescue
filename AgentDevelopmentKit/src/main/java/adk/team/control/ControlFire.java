package adk.team.control;

import rescuecore2.standard.entities.FireStation;
import rescuecore2.standard.entities.StandardEntity;

public abstract class ControlFire extends Control<FireStation> {
    public FireStation me;

    @Override
    public FireStation getOwner() {
        return this.me;
    }

    @Override
    public FireStation me() {
        return this.me;
    }

    @Override
    public StandardEntity getOwnerLocation() {
        return this.me;
    }
}
