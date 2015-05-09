package sample.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageFireBrigadeEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageFireBrigade;
import rescuecore2.standard.entities.FireBrigade;

public class SampleFireEvent implements MessageFireBrigadeEvent {

    private WorldProvider provider;
    private VictimSelectorProvider vsp;

    public SampleFireEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.provider = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageFireBrigade message) {
        FireBrigade fireBrigade = MessageReflectHelper.reflectedMessage(this.provider.getWorld(), message);
        this.vsp.getVictimSelector().add(fireBrigade);
    }

    @Override
    public void receivedVoice(MessageFireBrigade message) {
        this.receivedRadio(message);
    }
}
