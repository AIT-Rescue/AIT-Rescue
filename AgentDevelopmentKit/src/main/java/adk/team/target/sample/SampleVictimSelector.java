package comlib.adk.util.target.sample;

import comlib.adk.team.tactics.Tactics;
import comlib.adk.util.route.RouteUtil;
import comlib.adk.util.target.VictimSelector;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;


public class SampleVictimSelector extends VictimSelector {

    public Set<EntityID> civilianList;
    public Set<EntityID> agentList;

    public SampleVictimSelector(Tactics user) {
        super(user);
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
        StandardEntity entity = this.tactics.model.getEntity(id);
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
                StandardEntity civilian = this.tactics.model.getEntity(id);
                if(civilian != null) {
                    int d = RouteUtil.distance(this.tactics.model, this.tactics.me(), civilian);
                    if (minDistance >= d) {
                        minDistance = d;
                        result = id;
                    }
                }
            }
        }
        if(result == null) {
            for (EntityID id : this.agentList) {
                StandardEntity agent = this.tactics.model.getEntity(id);
                if(agent != null) {
                    int d = RouteUtil.distance(this.tactics.model, this.tactics.me(), agent);
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
