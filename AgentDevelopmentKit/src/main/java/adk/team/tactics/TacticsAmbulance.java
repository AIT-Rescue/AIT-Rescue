package adk.team.tactics;

import rescuecore2.standard.entities.AmbulanceTeam;

public abstract class TacticsAmbulance extends Tactics<AmbulanceTeam> {
    public AmbulanceTeam me;

    @Override
    public AmbulanceTeam me() {
        return this.me;
    }

}