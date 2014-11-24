package comlib.adk.team.tactics.basic.event;

import comlib.adk.team.tactics.basic.BasicAmbulance;
import comlib.event.information.PoliceForceMessageEvent;
import comlib.message.information.PoliceForceMessage;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicPoliceEvent implements PoliceForceMessageEvent{

    private BasicAmbulance tactics;

    public BasicPoliceEvent(BasicAmbulance basicAmbulance) {
        this.tactics = basicAmbulance;
    }

    @Override
    public void receivedRadio(PoliceForceMessage msg) {
        PoliceForce policeForce = this.reflectedMessage(this.tactics.model, msg);
        this.tactics.victimSelector.add(policeForce);
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
