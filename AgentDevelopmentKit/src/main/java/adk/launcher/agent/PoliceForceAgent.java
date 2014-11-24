package comlib.adk.agent;

import comlib.adk.team.tactics.PoliceForceTactics;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class PoliceForceAgent extends TacticsAgent<PoliceForceTactics, PoliceForce> {
    
    public static final String DISTANCE_KEY = "clear.repair.distance";
    
    public PoliceForceTactics pft;
    
    public PoliceForceAgent(PoliceForceTactics policeForceTactics) {
        super(policeForceTactics);
        this.pft = policeForceTactics;
    }
    
    @Override
    public String toString() {
        return "Comlib Police Force";
    }
    
    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.POLICE_FORCE);
    }
    
    @Override
    public void setAgentUniqueValue() {
        this.pft.distance = this.config.getIntValue(DISTANCE_KEY);
    }
    
    @Override
    public void setAgentEntity() {
        this.pft.me = this.me();
    }
}
