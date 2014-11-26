package adk.sample.basic.util;

import adk.team.util.BlockadeSelector;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;

public class BasicBlockadeSelector implements BlockadeSelector {

    public Set<EntityID> blockadeList;

    public WorldProvider<? extends Human> provider;

    public BasicBlockadeSelector(WorldProvider<? extends Human> user) {
        this.provider = user;
        this.blockadeList = new HashSet<>();
    }

    @Override
    public void add(Blockade blockade) {
        this.blockadeList.add(blockade.getID());
    }

    @Override
    public void add(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity != null) {
            if (entity instanceof Blockade) {
                this.blockadeList.add(id);
            }
        }
    }

    @Override
    public void remove(Blockade blockade) {
        this.blockadeList.remove(blockade.getID());
    }

    @Override
    public void remove(EntityID id) {
        this.blockadeList.remove(id);
    }

    @Override
    public EntityID getTarget(int time) {
        EntityID result = null;
        int minDistance = Integer.MAX_VALUE;
        for (EntityID id : this.blockadeList) {
            StandardEntity blockade = this.provider.getWorld().getEntity(id);
            if(blockade != null) {
                int d = this.provider.getWorld().getDistance(this.provider.me(), blockade);
                if (minDistance >= d) {
                    minDistance = d;
                    result = id;
                }
            }
        }
        return result;
    }
}
