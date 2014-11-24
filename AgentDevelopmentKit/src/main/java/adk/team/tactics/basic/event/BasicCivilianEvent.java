package comlib.adk.team.tactics.basic.event;

import comlib.adk.team.tactics.basic.BasicAmbulance;
import comlib.event.information.CivilianMessageEvent;
import comlib.message.information.CivilianMessage;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicCivilianEvent implements CivilianMessageEvent{

    private BasicAmbulance tactics;

    public BasicCivilianEvent(BasicAmbulance basicAmbulance) {
        this.tactics = basicAmbulance;
    }

    @Override
    public void receivedRadio(CivilianMessage msg) {
        Civilian civilian = reflectedMessage(this.tactics.model, msg);
        this.tactics.victimSelector.add(civilian);
    }

    @Override
    public void receivedVoice(CivilianMessage msg) {
        this.receivedRadio(msg);
    }

    public Civilian reflectedMessage(StandardWorldModel swm, CivilianMessage msg) {
        Civilian civilian = (Civilian)swm.getEntity(msg.getHumanID());
        if (civilian == null) {
            swm.addEntity(new Civilian(msg.getHumanID()));
            civilian = (Civilian) swm.getEntity(msg.getHumanID());
        }
        civilian.isHPDefined();
        civilian.isBuriednessDefined();
        civilian.isDamageDefined();
        civilian.isPositionDefined();
        civilian.setHP(msg.getHP());
        civilian.setBuriedness(msg.getBuriedness());
        civilian.setDamage(msg.getDamage());
        civilian.setPosition(msg.getPosition());

        return civilian;
    }
}
