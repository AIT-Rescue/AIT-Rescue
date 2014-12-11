package adk.sample.basic.event;

import adk.team.util.provider.DebrisRemovalSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.EventMessageRoad;
import comlib.message.information.MessageRoad;
import rescuecore2.standard.entities.Blockade;

public class BasicRoadEvent implements EventMessageRoad {

    private WorldProvider wp;
    private DebrisRemovalSelectorProvider drsp;

    public BasicRoadEvent(WorldProvider worldProvider, DebrisRemovalSelectorProvider debrisRemovalSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = debrisRemovalSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageRoad message) {
        if(message.getPassable()) {
            this.drsp.getDebrisRemovalSelector().remove(message.getRoadID());
        }
        else {
            Blockade blockade = this.wp.reflectedMessage(message);
            this.drsp.getDebrisRemovalSelector().add(blockade);
        }
    }

    @Override
    public void receivedVoice(MessageRoad message) {
        this.receivedRadio(message);
    }
}
