package comlib.adk.agent;

import comlib.adk.team.tactics.FireBrigadeTactics;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.StandardEntityURN;

import java.util.EnumSet;

public class FireBrigadeAgent extends TacticsAgent<FireBrigadeTactics, FireBrigade> {
    
    public static final String MAX_WATER_KEY = "fire.tank.maximum";
    public static final String MAX_DISTANCE_KEY = "fire.extinguish.max-distance";
    public static final String MAX_POWER_KEY = "fire.extinguish.max-sum";
    
    public FireBrigadeTactics fbt;
    
    public FireBrigadeAgent(FireBrigadeTactics fireBrigadeTactics) {
        super(fireBrigadeTactics);
        this.fbt = fireBrigadeTactics;
    }
    
    @Override
    public String toString() {
        return "Comlib Fire Brigade";
    }
    
    @Override
    protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum() {
        return EnumSet.of(StandardEntityURN.FIRE_BRIGADE);
    }

    @Override
    public void setAgentEntity() {
        this.fbt.me = this.me();
    }

    @Override
    public void setAgentUniqueValue() {
        this.fbt.maxWater = this.config.getIntValue(MAX_WATER_KEY);
        this.fbt.maxDistance = this.config.getIntValue(MAX_DISTANCE_KEY);
        this.fbt.maxPower = this.config.getIntValue(MAX_POWER_KEY);
        this.fbt.refugeList = this.getRefuges();
    }
}