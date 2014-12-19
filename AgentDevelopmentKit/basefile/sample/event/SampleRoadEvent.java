package sample.event;

import adk.team.util.provider.ImpassableSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.RoadMessageEvent;
import comlib.message.information.RoadMessage;
import rescuecore2.standard.entities.Blockade;

public class SampleRoadEvent implements RoadMessageEvent {

    private WorldProvider wp;
    private ImpassableSelectorProvider drsp;

    public SampleRoadEvent(WorldProvider worldProvider, ImpassableSelectorProvider impassableSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = impassableSelectorProvider;
    }

    @Override
    public void receivedRadio(RoadMessage msg) {
        Blockade blockade = this.wp.reflectedMessage(msg);
        this.drsp.getImpassableSelector().add(blockade);
    }

    @Override
    public void receivedVoice(RoadMessage msg) {
        this.receivedRadio(msg);
    }
}
