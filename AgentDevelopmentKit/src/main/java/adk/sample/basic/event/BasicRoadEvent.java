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
    public void receivedRadio(RoadMessage message) {
        if(message.getPassable()) {
            this.drsp.getDebrisRemovalSelector().remove(message.getRoadID());
        }
        else {
            Blockade blockade = this.wp.reflectedMessage(message);
            this.drsp.getDebrisRemovalSelector().add(blockade);
        }
    }

    @Override
    public void receivedVoice(RoadMessage message) {
        this.receivedRadio(message);
    }
}
