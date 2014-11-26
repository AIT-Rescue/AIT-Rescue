package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.PoliceForceMessageEvent;
import comlib.message.information.PoliceForceMessage;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicPoliceEvent implements PoliceForceMessageEvent{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicPoliceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(PoliceForceMessage msg) {
        PoliceForce policeForce = reflectedMessage(this.wp.getWorld(), msg);
        this.vsp.getVictimSelector().add(policeForce);
    }

    @Override
    public void receivedVoice(PoliceForceMessage msg) {
        this.receivedRadio(msg);
    }

    public PoliceForce reflectedMessage(StandardWorldModel swm, PoliceForceMessage msg) {
        PoliceForce policeforce = (PoliceForce) swm.getEntity(msg.getHumanID());
        if (policeforce == null) {
            swm.addEntity(new PoliceForce(msg.getHumanID()));
            policeforce = (PoliceForce) swm.getEntity(msg.getHumanID());
        }
        policeforce.isHPDefined();
        policeforce.isBuriednessDefined();
        policeforce.isDamageDefined();
        policeforce.isPositionDefined();
        policeforce.setHP(msg.getHP());
        policeforce.setBuriedness(msg.getBuriedness());
        policeforce.setDamage(msg.getDamage());
        policeforce.setPosition(msg.getPosition());

        return policeforce;
    }
}
