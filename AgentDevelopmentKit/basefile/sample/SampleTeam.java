package sample;

import adk.team.Team;
import adk.team.tactics.TacticsAmbulance;
import adk.team.tactics.TacticsFire;
import adk.team.tactics.TacticsPolice;
import sample.tactics.SampleTacticsAmbulance;
import sample.tactics.SampleTacticsFire;
import sample.tactics.SampleTacticsPolice;

public class SampleTeam extends Team {

    @Override
    public String getTeamName() {
        return "sample";
    }

    @Override
    public TacticsAmbulance getAmbulanceTeamTactics() {
        return new SampleTacticsAmbulance();
    }

    @Override
    public TacticsFire getFireBrigadeTactics() {
        return new SampleTacticsFire();
    }

    @Override
    public TacticsPolice getPoliceForceTactics() {
        return new SampleTacticsPolice();
    }
}
