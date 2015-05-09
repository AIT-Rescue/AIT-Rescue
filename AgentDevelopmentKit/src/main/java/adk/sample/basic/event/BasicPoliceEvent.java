package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessagePoliceForceEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessagePoliceForce;
import rescuecore2.standard.entities.PoliceForce;

public class BasicPoliceEvent implements MessagePoliceForceEvent {

    private WorldProvider provider;
    private VictimSelectorProvider vsp;

    public BasicPoliceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.provider = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessagePoliceForce message) {
        PoliceForce policeForce = MessageReflectHelper.reflectedMessage(this.provider.getWorld(), message);
        this.vsp.getVictimSelector().add(policeForce);
    }

    @Override
    public void receivedVoice(MessagePoliceForce message) {
        this.receivedRadio(message);
    }
}
