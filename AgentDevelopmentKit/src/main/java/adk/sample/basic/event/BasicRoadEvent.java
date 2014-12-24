package adk.sample.basic.event;

import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageRoadEvent;
import comlib.manager.UpdateHelper;
import comlib.message.information.MessageRoad;
import rescuecore2.standard.entities.Blockade;

public class BasicRoadEvent implements MessageRoadEvent {

    private WorldProvider wp;
    private ImpassableSelectorProvider drsp;

    public BasicRoadEvent(WorldProvider worldProvider, ImpassableSelectorProvider impassableSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = impassableSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageRoad message) {
        if(message.isPassable()) {
            this.drsp.getImpassableSelector().remove(message.getRoadID());
        }
        else {
            Blockade blockade = UpdateHelper.reflectedMessage(this.wp.getWorld(), message);
            this.drsp.getImpassableSelector().add(blockade);
        }
    }

    @Override
    public void receivedVoice(MessageRoad message) {
        this.receivedRadio(message);
    }
}
