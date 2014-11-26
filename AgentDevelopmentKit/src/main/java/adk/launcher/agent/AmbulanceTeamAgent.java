package adk.launcher.agent;

import adk.team.tactics.TacticsAmbulance;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.ChangeSet;

import java.util.EnumSet;

public class AmbulanceTeamAgent extends TacticsAgent<TacticsAmbulance, AmbulanceTeam> {
    
    private TacticsAmbulance ta;
    
    public AmbulanceTeamAgent(TacticsAmbulance tacticsAmbulance) {
        super(tacticsAmbulance);
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
    public void receiveBeforeEvent(int time, ChangeSet changed) {
        super.receiveBeforeEvent(time, changed);
        this.ta.location = this.location();
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
        this.ta.refugeList = this.getRefuges();
    }

    @Override
    protected void setAgentEntity() {
        this.ta.me = this.me();
    }

    @Override
    protected AmbulanceTeam me() {
        return (AmbulanceTeam)this.model.getEntity(this.getID());
    }

    @Override
    protected StandardEntity location() {
        return this.me().getPosition(this.model);
    }
}