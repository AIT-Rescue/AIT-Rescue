package comlib.adk.util.target.sample;

import comlib.adk.team.tactics.Tactics;
import comlib.adk.util.route.RouteUtil;
import comlib.adk.util.target.BlockadeSelector;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

import java.util.HashSet;
import java.util.Set;

public class SampleBlockadeSelector extends BlockadeSelector {

    public Set<EntityID> blockadeList;

    public SampleBlockadeSelector(Tactics user) {
        super(user);
        this.blockadeList = new HashSet<>();
    }

    @Override
    public void add(Blockade blockade) {
        this.blockadeList.add(blockade.getID());
    }

    @Override
    public void add(EntityID id) {
        StandardEntity entity = this.tactics.model.getEntity(id);
        if(entity instanceof Blockade) {
            this.blockadeList.add(id);
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
            StandardEntity blockade = this.tactics.model.getEntity(id);
            if(blockade != null) {
                int d = RouteUtil.distance(this.tactics.model, this.tactics.me(), blockade);
                if (minDistance >= d) {
                    minDistance = d;
                    result = id;
                }
            }
        }
        return result;
    }
}
