package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.CivilianMessageEvent;
import comlib.message.information.CivilianMessage;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicCivilianEvent implements CivilianMessageEvent{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicCivilianEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(CivilianMessage msg) {
        Civilian civilian = reflectedMessage(this.wp.getWorld(), msg);
        this.vsp.getVictimSelector().add(civilian);
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
