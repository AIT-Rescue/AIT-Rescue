package adk.sample.basic.event;

import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.EventMessageRoad;
import comlib.message.information.MessageRoad;
import rescuecore2.standard.entities.Blockade;

public class BasicRoadEvent implements EventMessageRoad {

    private WorldProvider wp;
    private ImpassableSelectorProvider drsp;

    public BasicRoadEvent(WorldProvider worldProvider, ImpassableSelectorProvider impassableSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = impassableSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageRoad message) {
        if(message.getPassable()) {
            this.drsp.getImpassableSelector().remove(message.getRoadID());
        }
        else {
            Blockade blockade = this.wp.reflectedMessage(message);
            this.drsp.getImpassableSelector().add(blockade);
        }
    }

    @Override
    public void receivedVoice(MessageRoad message) {
        this.receivedRadio(message);
    }
}
