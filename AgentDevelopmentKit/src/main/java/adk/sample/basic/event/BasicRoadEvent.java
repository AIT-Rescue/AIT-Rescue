package adk.sample.basic.event;

import adk.team.util.provider.DebrisRemovalSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.RoadMessageEvent;
import comlib.message.information.RoadMessage;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicRoadEvent implements RoadMessageEvent {

    private WorldProvider wp;
    private DebrisRemovalSelectorProvider drsp;

    public BasicRoadEvent(WorldProvider worldProvider, DebrisRemovalSelectorProvider debrisRemovalSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = debrisRemovalSelectorProvider;
    }

    @Override
    public void receivedRadio(RoadMessage msg) {
        Blockade blockade = this.reflectedMessage(this.wp.getWorld(), msg);
        this.drsp.getDebrisRemovalSelector().add(blockade);
    }

    @Override
    public void receivedVoice(RoadMessage msg) {
        this.receivedRadio(msg);
    }

    public Blockade  reflectedMessage(StandardWorldModel world, RoadMessage msg) {
        Blockade blockade = (Blockade) world.getEntity(msg.getID());
        if (blockade == null) {
            world.addEntity(new Blockade(msg.getID()));
            blockade = (Blockade) world.getEntity(msg.getID());
        }
        blockade.isPositionDefined();
        blockade.isRepairCostDefined();
        blockade.setPosition(msg.getPosition());
        blockade.setRepairCost(msg.getRepairCost());

        return blockade;
    }
}
