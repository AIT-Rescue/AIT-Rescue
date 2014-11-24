package comlib.adk.team.tactics.basic.event;

import comlib.adk.team.tactics.basic.BasicPolice;
import comlib.event.information.RoadMessageEvent;
import comlib.message.information.RoadMessage;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicRoadEvent implements RoadMessageEvent {

    private BasicPolice tactics;

    public BasicRoadEvent(BasicPolice basicPolice) {
        this.tactics = basicPolice;
    }

    @Override
    public void receivedRadio(RoadMessage msg) {
        Blockade blockade = this.reflectedMessage(this.tactics.model, msg);
        this.tactics.blockadeSelector.add(blockade);
    }

    @Override
    public void receivedVoice(RoadMessage msg) {
        this.receivedRadio(msg);
    }

    public Blockade  reflectedMessage(StandardWorldModel world, RoadMessage msg) {
        Blockade blockade = (Blockade) world.getEntity(msg.getID());
        if (blockade == null) {
            world.addEntity(new Blockade(msg.getID()));
            blockade = (Blockade) world.getEntity(msg.getID());
        }
        blockade.isPositionDefined();
        blockade.isRepairCostDefined();
        blockade.setPosition(msg.getPosition());
        blockade.setRepairCost(msg.getRepairCost());

        return blockade;
    }
}
