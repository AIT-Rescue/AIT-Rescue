package sample.event;

import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageBuildingEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageBuilding;

public class SampleBuildingEvent implements MessageBuildingEvent{

    private WorldProvider wp;
    private BuildingSelectorProvider bsp;

    public SampleBuildingEvent(WorldProvider worldProvider, BuildingSelectorProvider buildingSelectorProvider) {
        this.wp = worldProvider;
        this.bsp = buildingSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageBuilding message) {
        this.bsp.getBuildingSelector().add(MessageReflectHelper.reflectedMessage(this.wp.getWorld(), message));
    }

    @Override
    public void receivedVoice(MessageBuilding message) {
        this.receivedRadio(message);
    }
}
