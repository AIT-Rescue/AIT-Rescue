package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.EventMessagePoliceForce;
import comlib.message.information.MessagePoliceForce;
import rescuecore2.standard.entities.PoliceForce;

public class BasicPoliceEvent implements EventMessagePoliceForce{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicPoliceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessagePoliceForce message) {
        PoliceForce policeForce = this.wp.reflectedMessage(message);
        this.vsp.getVictimSelector().add(policeForce);
    }

    @Override
    public void receivedVoice(MessagePoliceForce message) {
        this.receivedRadio(message);
    }
}
