package adk.sample.basic.event;

import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.BuildingMessageEvent;
import comlib.message.information.BuildingMessage;

public class BasicBuildingEvent implements BuildingMessageEvent{

    private WorldProvider wp;
    private BuildingSelectorProvider bsp;

    public BasicBuildingEvent(WorldProvider worldProvider, BuildingSelectorProvider buildingSelectorProvider) {
        this.wp = worldProvider;
        this.bsp = buildingSelectorProvider;
    }

    @Override
    public void receivedRadio(BuildingMessage message) {
        this.bsp.getBuildingSelector().add(this.wp.reflectedMessage(message));
    }

    @Override
    public void receivedVoice(BuildingMessage message) {
        this.receivedRadio(message);
    }
}
