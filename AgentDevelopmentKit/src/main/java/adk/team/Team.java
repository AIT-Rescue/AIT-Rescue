package adk.team;

public abstract class Team {
    public abstract String getTeamName();

    public abstract AmbulanceTeamTactics getAmbulanceTeamTactics();

    public abstract FireBrigadeTactics getFireBrigadeTactics();

    public abstract PoliceForceTactics getPoliceForceTactics();

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
