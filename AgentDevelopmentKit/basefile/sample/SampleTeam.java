package sample;

import adk.team.Team;
import adk.team.tactics.TacticsAmbulance;
import adk.team.tactics.TacticsFire;
import adk.team.tactics.TacticsPolice;
import sample.tactics.SampleAmbulance;
import sample.tactics.SampleFire;
import sample.tactics.SamplePolice;

public class SampleTeam extends Team {

    @Override
    public String getTeamName() {
        return "sample";
    }

    @Override
    public TacticsAmbulance getAmbulanceTeamTactics() {
        return new SampleAmbulance();
    }

    @Override
    public TacticsFire getFireBrigadeTactics() {
        return new SampleFire();
    }

    @Override
    public TacticsPolice getPoliceForceTactics() {
        return new SamplePolice();
    }
}
