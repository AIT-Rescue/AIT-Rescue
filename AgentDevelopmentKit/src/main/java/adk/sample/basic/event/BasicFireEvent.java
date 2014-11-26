package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.FireBrigadeMessageEvent;
import comlib.message.information.FireBrigadeMessage;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicFireEvent implements FireBrigadeMessageEvent{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicFireEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(FireBrigadeMessage msg) {
        FireBrigade fireBrigade = reflectedMessage(this.wp.getWorld(), msg);
        this.vsp.getVictimSelector().add(fireBrigade);
    }

    @Override
    public void receivedVoice(FireBrigadeMessage msg) {
        this.receivedRadio(msg);
    }

    public FireBrigade reflectedMessage(StandardWorldModel swm, FireBrigadeMessage msg) {
        FireBrigade firebrigade = (FireBrigade) swm.getEntity(msg.getHumanID());
        if (firebrigade == null) {
            swm.addEntity(new FireBrigade(msg.getHumanID()));
            firebrigade = (FireBrigade) swm.getEntity(msg.getHumanID());
        }
        firebrigade.isHPDefined();
        firebrigade.isBuriednessDefined();
        firebrigade.isDamageDefined();
        firebrigade.isPositionDefined();
        firebrigade.setHP(msg.getHP());
        firebrigade.setBuriedness(msg.getBuriedness());
        firebrigade.setDamage(msg.getDamage());
        firebrigade.setPosition(msg.getPosition());
        firebrigade.setWater(msg.getWater());
        return firebrigade;
    }
}
