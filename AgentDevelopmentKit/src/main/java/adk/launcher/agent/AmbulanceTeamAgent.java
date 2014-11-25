package adk.launcher.agent;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class AmbulanceTeamAgent extends TacticsAgent<TacticsAmbulance, AmbulanceTeam> {
    
    public TacticsAmbulance att;
    
    public AmbulanceTeamAgent(TacticsAmbulance ambulanceTactics) {
        super(ambulanceTactics);
        this.att = ambulanceTactics;
    }
    
    @Override
    public String toString() {
        return "Comlib Ambulance Team";
    }
    
    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.AMBULANCE_TEAM);
    }

    @Override
    public void setAgentEntity() {
        this.att.me = this.me();
    }

    @Override
    public void setAgentUniqueValue() {
        this.att.refugeList = this.getRefuges();
    }
}