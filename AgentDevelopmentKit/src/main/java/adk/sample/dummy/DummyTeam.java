package adk.sample.dummy;

import adk.sample.dummy.tactics.DummyAmbulance;
import adk.sample.dummy.tactics.DummyFire;
import adk.sample.dummy.tactics.DummyPolice;
import adk.team.Team;
import adk.team.tactics.TacticsAmbulance;
import adk.team.tactics.TacticsFire;
import adk.team.tactics.TacticsPolice;

public class DummyTeam extends Team {

    @Override
    public String getTeamName() {
        return "dummy";
    }

    @Override
    public TacticsAmbulance getAmbulanceTeamTactics() {
        return new DummyAmbulance();
    }

    @Override
    public TacticsFire getFireBrigadeTactics() {
        return new DummyFire();
    }

    @Override
    public TacticsPolice getPoliceForceTactics() {
        return new DummyPolice();
    }
}
