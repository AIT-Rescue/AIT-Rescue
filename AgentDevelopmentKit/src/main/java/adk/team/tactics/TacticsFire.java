package adk.team.tactics;

import rescuecore2.standard.entities.FireBrigade;

public abstract class TacticsFire extends Tactics<FireBrigade> {
    public FireBrigade me;

    public int maxWater;
    public int maxDistance;
    public int maxPower;

    @Override
    public FireBrigade me() {
        return this.me;
    }
}