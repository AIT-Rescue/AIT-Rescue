package adk.team.tactics;

import rescuecore2.standard.entities.PoliceForce;

public abstract class TacticsPolice extends Tactics<PoliceForce> {
    public PoliceForce me;

    public int distance;

    @Override
    public PoliceForce getOwner() {
        return this.me;
    }

    @Override
    public PoliceForce me() {
        return this.getOwner();
    }
}