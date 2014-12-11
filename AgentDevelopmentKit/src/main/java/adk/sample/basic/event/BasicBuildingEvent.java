package adk.sample.basic.event;

import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.EventMessageBuilding;
import comlib.message.information.MessageBuilding;

public class BasicBuildingEvent implements EventMessageBuilding{

    private WorldProvider wp;
    private BuildingSelectorProvider bsp;

    public BasicBuildingEvent(WorldProvider worldProvider, BuildingSelectorProvider buildingSelectorProvider) {
        this.wp = worldProvider;
        this.bsp = buildingSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageBuilding message) {
        this.bsp.getBuildingSelector().add(this.wp.reflectedMessage(message));
    }

    @Override
    public void receivedVoice(MessageBuilding message) {
        this.receivedRadio(message);
    }
}
