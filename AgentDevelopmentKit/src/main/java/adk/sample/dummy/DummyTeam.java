package adk.sample.dummy;

import adk.sample.dummy.tactics.DummyTacticsAmbulance;
import adk.sample.dummy.tactics.DummyTacticsFire;
import adk.sample.dummy.tactics.DummyTacticsPolice;
import adk.team.Team;
import adk.team.precompute.*;
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
        return new DummyTacticsAmbulance();
    }

    @Override
    public TacticsFire getFireBrigadeTactics() {
        return new DummyTacticsFire();
    }

    @Override
    public TacticsPolice getPoliceForceTactics() {
        return new DummyTacticsPolice();
    }

    @Override
    public PrecomputeAmbulance getAmbulancePrecompute() {
        return null;
    }

    @Override
    public PrecomputeFire getFirePrecompute() {
        return null;
    }

    @Override
    public PrecomputePolice getPolicePrecompute() {
        return null;
    }

}
