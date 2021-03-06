package adk.launcher.agent;

import adk.team.tactics.TacticsPolice;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class PoliceForceAgent extends TacticsAgent<PoliceForce> {
    
    public static final String DISTANCE_KEY = "clear.repair.distance";
    
    private TacticsPolice tp;
    
    public PoliceForceAgent(TacticsPolice tacticsPolice, boolean pre) {
        super(tacticsPolice, pre);
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
    protected void setAgentUniqueValue() {
        this.model.indexClass(
                StandardEntityURN.REFUGE,
                StandardEntityURN.ROAD,
                StandardEntityURN.BUILDING,
                StandardEntityURN.POLICE_FORCE
        );
        this.tp.distance = this.config.getIntValue(DISTANCE_KEY);
    }
    
    @Override
    protected void setAgentEntity() {
        this.tp.me = this.me();
        this.tp.location = this.tp.me.getPosition(this.model);
    }

    @Override
    protected PoliceForce me() {
        try {
            return (PoliceForce) this.model.getEntity(this.getID());
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
