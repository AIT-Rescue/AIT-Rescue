package adk.launcher.agent;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class AmbulanceTeamAgent extends TacticsAgent<AmbulanceTeam> {
    
    private TacticsAmbulance ta;
    
    public AmbulanceTeamAgent(TacticsAmbulance tacticsAmbulance, boolean pre) {
        super(tacticsAmbulance, pre);
        this.ta = tacticsAmbulance;
    }
    
    @Override
    public String toString() {
        return "Ambulance Team [tactics: " + this.ta.getTacticsName() + " ]";
    }
    
    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.AMBULANCE_TEAM);
    }

    @Override
    protected void setAgentUniqueValue() {
        this.model.indexClass(
                StandardEntityURN.CIVILIAN,
                StandardEntityURN.REFUGE,
                StandardEntityURN.ROAD,
                StandardEntityURN.BUILDING,
                StandardEntityURN.AMBULANCE_TEAM,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.POLICE_FORCE
        );
    }

    @Override
    protected void setAgentEntity() {
        this.ta.me = this.me();
        this.ta.location = this.ta.me.getPosition(this.model);
    }

    @Override
    protected AmbulanceTeam me() {
        try {
            return (AmbulanceTeam) this.model.getEntity(this.getID());
        }
        catch(NullPointerException e) {
            return null;
        }
    }

    @Override
    protected StandardEntity location() {
        try {
            return this.me().getPosition(this.model);
        }
        catch(NullPointerException e) {
            return null;
        }
    }
}