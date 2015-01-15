package adk.launcher.agent;

import adk.team.tactics.TacticsFire;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class FireBrigadeAgent extends TacticsAgent<FireBrigade> {
    
    public static final String MAX_WATER_KEY = "fire.tank.maximum";
    public static final String MAX_DISTANCE_KEY = "fire.extinguish.max-distance";
    public static final String MAX_POWER_KEY = "fire.extinguish.max-sum";
    
    private TacticsFire tf;
    
    public FireBrigadeAgent(TacticsFire tacticsFire, boolean pre) {
        super(tacticsFire, pre);
        this.tf = tacticsFire;
    }
    
    @Override
    public String toString() {
        return "Fire Brigade [tactics: " + this.tf.getTacticsName() + " ]";
    }
    
    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.FIRE_BRIGADE);
    }

    @Override
    protected void setAgentUniqueValue() {
        this.model.indexClass(
                StandardEntityURN.BUILDING,
                StandardEntityURN.REFUGE,
                StandardEntityURN.ROAD,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.GAS_STATION
        );
        this.tf.maxWater = this.config.getIntValue(MAX_WATER_KEY);
        this.tf.maxDistance = this.config.getIntValue(MAX_DISTANCE_KEY);
        this.tf.maxPower = this.config.getIntValue(MAX_POWER_KEY);
    }

    @Override
    protected void setAgentEntity() {
        this.tf.me = this.me();
        this.tf.location = this.tf.me.getPosition(this.model);
    }

    @Override
    protected FireBrigade me() {
        try {
            return (FireBrigade)this.model.getEntity(this.getID());
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