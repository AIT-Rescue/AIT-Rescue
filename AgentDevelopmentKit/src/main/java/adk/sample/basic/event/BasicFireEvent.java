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
    public void receivedRadio(FireBrigadeMessage message) {
        FireBrigade fireBrigade = this.wp.reflectedMessage(message);
        this.vsp.getVictimSelector().add(fireBrigade);
    }

    @Override
    public void receivedVoice(FireBrigadeMessage message) {
        this.receivedRadio(message);
    }
}
