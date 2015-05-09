package adk.sample.basic.event;

import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageRoadEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageRoad;
import rescuecore2.standard.entities.Blockade;

public class BasicRoadEvent implements MessageRoadEvent {

    private WorldProvider provider;
    private ImpassableSelectorProvider drsp;

    public BasicRoadEvent(WorldProvider worldProvider, ImpassableSelectorProvider impassableSelectorProvider) {
        this.provider = worldProvider;
        this.drsp = impassableSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageRoad message) {
        if(message.isPassable()) {
            this.drsp.getImpassableSelector().remove(message.getRoadID());
        }
        else {
            Blockade blockade = MessageReflectHelper.reflectedMessage(this.provider.getWorld(), message);
            this.drsp.getImpassableSelector().add(blockade);
        }
    }

    @Override
    public void receivedVoice(MessageRoad message) {
        this.receivedRadio(message);
    }
}
