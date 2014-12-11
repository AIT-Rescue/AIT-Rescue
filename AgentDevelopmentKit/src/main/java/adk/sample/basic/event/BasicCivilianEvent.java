package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.EventMessageCivilian;
import comlib.message.information.MessageCivilian;
import rescuecore2.standard.entities.Civilian;

public class BasicCivilianEvent implements EventMessageCivilian{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicCivilianEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageCivilian message) {
        Civilian civilian = this.wp.reflectedMessage(message);
        this.vsp.getVictimSelector().add(civilian);
    }

    @Override
    public void receivedVoice(MessageCivilian message) {
        this.receivedRadio(message);
    }
}
