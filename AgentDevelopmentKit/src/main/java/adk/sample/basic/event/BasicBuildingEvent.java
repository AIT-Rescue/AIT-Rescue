package adk.sample.basic.event;

import adk.team.util.provider.BuildingSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.BuildingMessageEvent;
import comlib.message.information.BuildingMessage;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicBuildingEvent implements BuildingMessageEvent{

    private WorldProvider wp;
    private BuildingSelectorProvider bsp;

    public BasicBuildingEvent(WorldProvider worldProvider, BuildingSelectorProvider buildingSelectorProvider) {
        this.wp = worldProvider;
        this.bsp = buildingSelectorProvider;
    }

    @Override
    public void receivedRadio(BuildingMessage msg) {
        this.bsp.getBuildingSelector().add(this.reflectedMessage(this.wp.getWorld(), msg));
    }

    @Override
    public void receivedVoice(BuildingMessage msg) {
        this.receivedRadio(msg);
    }

    public Building reflectedMessage(StandardWorldModel swm, BuildingMessage msg) {
        Building building = (Building) swm.getEntity(msg.getBuildingID());
        building.isFierynessDefined();
        building.isBrokennessDefined();
        building.setFieryness(msg.getFieryness());
        building.setBrokenness(msg.getBrokenness());

        return building;
    }
}
