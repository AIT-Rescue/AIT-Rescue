package adk.sample.basic.event;

import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageBuildingEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageBuilding;

public class BasicBuildingEvent implements MessageBuildingEvent{

    private WorldProvider provider;
    private BuildingSelectorProvider bsp;

    public BasicBuildingEvent(WorldProvider worldProvider, BuildingSelectorProvider buildingSelectorProvider) {
        this.provider = worldProvider;
        this.bsp = buildingSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageBuilding message) {
        this.bsp.getBuildingSelector().add(MessageReflectHelper.reflectedMessage(this.provider.getWorld(), message));
    }

    @Override
    public void receivedVoice(MessageBuilding message) {
        this.receivedRadio(message);
    }
}
