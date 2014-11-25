package adk.team;

import adk.team.tactics.TacticsAmbulance;
import adk.team.tactics.TacticsFire;
import adk.team.tactics.TacticsPolice;

public abstract class Team {
    public abstract String getTeamName();

    public abstract TacticsAmbulance getAmbulanceTeamTactics();

    public abstract TacticsFire getFireBrigadeTactics();

    public abstract TacticsPolice getPoliceForceTactics();

    /*
    public FireStationTactics getFireStationTactics() {
        return null;
    }
    public AmbulanceCentreTactics getAmbulanceCentreTactics() {
        return null;
    }
   public PoliceOfficeTactics getPoliceOfficeTactics() {
       return null;
   }
   */
}
