package adk.sample.basic.util;

import adk.team.util.BlockadeSelector;
import adk.team.util.PositionUtil;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;

public class BasicBlockadeSelector implements BlockadeSelector {

    public Set<Blockade> blockadeList;

    public WorldProvider<? extends StandardEntity> provider;

    public BasicBlockadeSelector(WorldProvider<? extends StandardEntity> user) {
        this.provider = user;
        this.blockadeList = new HashSet<>();
    }

    @Override
    public void add(Blockade blockade) {
        this.blockadeList.add(blockade);
    }

    @Override
    public void add(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity != null && entity instanceof Blockade) {
            this.blockadeList.add((Blockade)entity);
        }
    }

    @Override
    public void remove(Blockade blockade) {
        this.blockadeList.remove(blockade);
    }

    @Override
    public void remove(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity != null && entity instanceof Blockade) {
            this.blockadeList.remove(entity);
        }
    }

    @Override
    public EntityID getTarget(int time) {
        Blockade result = null;
        StandardEntity owner = this.provider.getOwner();
        StandardWorldModel world = this.provider.getWorld();
        for(Blockade blockade : this.blockadeList) {
            if(result != null) {
                result = (Blockade)PositionUtil.getNearEntity(owner, result, blockade, world);
            }
            else {
                result = blockade;
            }
        }
        return result != null ? result.getID() : null;
    }
}
