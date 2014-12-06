package sample.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.PoliceForceMessageEvent;
import comlib.message.information.PoliceForceMessage;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardWorldModel;

public class SamplePoliceEvent implements PoliceForceMessageEvent{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public SamplePoliceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(PoliceForceMessage message) {
        PoliceForce policeForce = this.wp.reflectedMessage(message);
        this.vsp.getVictimSelector().add(policeForce);
    }

    @Override
    public void receivedVoice(PoliceForceMessage message) {
        this.receivedRadio(message);
    }
}
