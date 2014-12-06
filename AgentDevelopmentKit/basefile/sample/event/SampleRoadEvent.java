package sample.event;

import adk.team.util.provider.DebrisRemovalSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.RoadMessageEvent;
import comlib.message.information.RoadMessage;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.StandardWorldModel;

public class SampleRoadEvent implements RoadMessageEvent {

    private WorldProvider wp;
    private DebrisRemovalSelectorProvider drsp;

    public SampleRoadEvent(WorldProvider worldProvider, DebrisRemovalSelectorProvider debrisRemovalSelectorProvider) {
        this.wp = worldProvider;
        this.drsp = debrisRemovalSelectorProvider;
    }

    @Override
    public void receivedRadio(RoadMessage msg) {
        Blockade blockade = this.wp.reflectedMessage(msg);
        this.drsp.getDebrisRemovalSelector().add(blockade);
    }

    @Override
    public void receivedVoice(RoadMessage msg) {
        this.receivedRadio(msg);
    }
}
