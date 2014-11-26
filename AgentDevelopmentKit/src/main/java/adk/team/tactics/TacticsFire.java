package adk.team.tactics;

import rescuecore2.standard.entities.FireBrigade;

public abstract class TacticsFire extends Tactics<FireBrigade> {
    public FireBrigade me;

    public int maxWater;
    public int maxDistance;
    public int maxPower;

    @Override
    public FireBrigade getOwner() {
        return this.me;
    }

    @Override
    public FireBrigade me() {
        return this.getOwner();
    }
}