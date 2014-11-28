package adk.sample.basic.util;

import adk.team.util.PositionUtil;
import adk.team.util.VictimSelector;
import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;


public class BasicVictimSelector implements VictimSelector {

    public WorldProvider<? extends StandardEntity> provider;

    public Set<Civilian> civilianList;
    public Set<Human> agentList;

    public BasicVictimSelector(WorldProvider<? extends StandardEntity> user) {
        this.provider = user;
        this.civilianList = new HashSet<>();
        this.agentList = new HashSet<>();
    }

    @Override
    public void add(Civilian civilian) {
        if(civilian.getBuriedness() > 0) {
            this.civilianList.add(civilian);
        }
        else {
            this.civilianList.remove(civilian);
        }
    }

    @Override
    public void add(Human agent) {
        if(agent.getBuriedness() > 0) {
            this.agentList.add(agent);
        }
        else {
            this.agentList.remove(agent);
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
        this.civilianList.remove(civilian);
    }

    @Override
    public void remove(Human agent) {
        this.agentList.remove(agent);
    }

    @Override
    public void remove(EntityID id) {
        StandardEntity entity = this.provider.getWorld().getEntity(id);
        if(entity instanceof Civilian) {
            this.civilianList.remove(entity);
            this.agentList.remove(entity);
        }
        else if(entity instanceof Human) {
            this.agentList.remove(entity);
        }
    }

    @Override
    public EntityID getTarget(int time) {
        //this.routeSearch.getPath(time, this.tactics.me, this.civilianList.get(0));
        /*EntityID result = null;
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
        return result;*/
        Human result = null;
        StandardEntity owner = this.provider.getOwner();
        StandardWorldModel world = this.provider.getWorld();
        for(Civilian civilian : this.civilianList) {
            if(result != null) {
                result = (Human) PositionUtil.getNearEntity(owner, result, civilian, world);
            }
            else {
                result = civilian;
            }
        }
        if(result == null) {
            for(Human agent : this.agentList) {
                if(result != null) {
                    result = (Human) PositionUtil.getNearEntity(owner, result, agent, world);
                }
                else {
                    result = agent;
                }
            }
        }
        return result != null ? result.getID() : null;
    }
}
