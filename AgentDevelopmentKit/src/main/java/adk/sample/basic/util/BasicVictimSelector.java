package adk.sample.basic.util;

import adk.team.util.VictimSelector;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;


public class BasicVictimSelector implements VictimSelector {

    public WorldProvider<? extends Human> provider;

    public Set<EntityID> civilianList;
    public Set<EntityID> agentList;

    public BasicVictimSelector(WorldProvider<? extends Human> user) {
        this.provider = user;
        this.civilianList = new HashSet<>();
        this.agentList = new HashSet<>();
    }

    @Override
    public void add(Civilian civilian) {
        if(civilian.getBuriedness() > 0) {
            this.civilianList.add(civilian.getID());
        }
        else {
            this.civilianList.remove(civilian.getID());
        }
    }

    @Override
    public void add(Human agent) {
        if(agent.getBuriedness() > 0) {
            this.agentList.add(agent.getID());
        }
        else {
            this.agentList.remove(agent.getID());
        }
    }

    @Override
    public void add(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity instanceof Civilian) {
            this.add((Civilian)entity);
        }
        else if(entity instanceof Human) {
            this.add((Human)entity);
        }

    }

    @Override
    public void remove(Civilian civilian) {
        this.civilianList.remove(civilian.getID());
    }

    @Override
    public void remove(Human agent) {
        this.agentList.remove(agent.getID());
    }

    @Override
    public void remove(EntityID id) {
        this.civilianList.remove(id);
        this.agentList.remove(id);
    }

    @Override
    public EntityID getTarget(int time) {
        //this.routeSearch.getPath(time, this.tactics.me, this.civilianList.get(0));
        EntityID result = null;
        int minDistance = Integer.MAX_VALUE;
        if(!this.civilianList.isEmpty()) {
            for (EntityID id : this.civilianList) {
                StandardEntity civilian = this.provider.getWorld().getEntity(id);
                if(civilian != null) {
                    int d = this.provider.getWorld().getDistance(this.provider.me(), civilian);
                    if (minDistance >= d) {
                        minDistance = d;
                        result = id;
                    }
                }
            }
        }
        if(result == null) {
            for (EntityID id : this.agentList) {
                StandardEntity agent = this.provider.getWorld().getEntity(id);
                if(agent != null) {
                    int d = this.provider.getWorld().getDistance(this.provider.me(), agent);
                    if (minDistance >= d) {
                        minDistance = d;
                        result = id;
                    }
                }
            }
        }
        return result;
    }
}
