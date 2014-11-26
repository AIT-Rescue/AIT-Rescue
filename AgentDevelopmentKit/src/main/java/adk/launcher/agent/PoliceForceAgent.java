package adk.launcher.agent;

import adk.team.tactics.TacticsPolice;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.ChangeSet;

import java.util.EnumSet;

public class PoliceForceAgent extends TacticsAgent<TacticsPolice, PoliceForce> {
    
    public static final String DISTANCE_KEY = "clear.repair.distance";
    
    private TacticsPolice tp;
    
    public PoliceForceAgent(TacticsPolice tacticsPolice) {
        super(tacticsPolice);
        this.tp = tacticsPolice;
    }
    
    @Override
    public String toString() {
        return "Police Force [tactics: " + this.tp.getTacticsName() + " ]";
    }
    
    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.POLICE_FORCE);
    }

    @Override
    public void receiveBeforeEvent(int time, ChangeSet changed) {
        super.receiveBeforeEvent(time, changed);
        this.tp.location = this.location();
    }
    
    @Override
    protected void setAgentUniqueValue() {
        this.model.indexClass(
                StandardEntityURN.REFUGE,
                StandardEntityURN.ROAD,
                StandardEntityURN.BUILDING,
                StandardEntityURN.POLICE_FORCE
        );
        this.tp.distance = this.config.getIntValue(DISTANCE_KEY);
        this.tp.refugeList = this.getRefuges();
    }
    
    @Override
    protected void setAgentEntity() {
        this.tp.me = this.me();
    }

    @Override
    protected PoliceForce me() {
        return (PoliceForce)this.model.getEntity(this.getID());
    }

    @Override
    protected StandardEntity location() {
        return this.me().getPosition(this.model);
    }
}
