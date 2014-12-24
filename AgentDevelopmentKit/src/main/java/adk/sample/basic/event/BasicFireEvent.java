package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageFireBrigadeEvent;
import comlib.manager.UpdateHelper;
import comlib.message.information.MessageFireBrigade;
import rescuecore2.standard.entities.FireBrigade;

public class BasicFireEvent implements MessageFireBrigadeEvent {

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicFireEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageFireBrigade message) {
        FireBrigade fireBrigade = UpdateHelper.reflectedMessage(this.wp.getWorld(), message);
        this.vsp.getVictimSelector().add(fireBrigade);
    }

    @Override
    public void receivedVoice(MessageFireBrigade message) {
        this.receivedRadio(message);
    }
}
