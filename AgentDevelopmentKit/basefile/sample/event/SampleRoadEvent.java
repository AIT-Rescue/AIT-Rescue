package sample.event;

import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageRoadEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageRoad;
import rescuecore2.standard.entities.Blockade;

public class SampleRoadEvent implements MessageRoadEvent {

    private WorldProvider wp;
    private ImpassableSelectorProvider drsp;

    public SampleRoadEvent(WorldProvider worldProvider, ImpassableSelectorProvider impassableSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = impassableSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageRoad message) {
        if(message.isPassable()) {
            this.drsp.getImpassableSelector().remove(message.getRoadID());
        }
        else {
            Blockade blockade = MessageReflectHelper.reflectedMessage(this.wp.getWorld(), message);
            this.drsp.getImpassableSelector().add(blockade);
        }
    }

    @Override
    public void receivedVoice(MessageRoad message) {
        this.receivedRadio(message);
    }
}
