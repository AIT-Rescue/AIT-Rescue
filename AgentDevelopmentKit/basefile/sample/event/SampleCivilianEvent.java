package sample.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.CivilianMessageEvent;
import comlib.message.information.CivilianMessage;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.StandardWorldModel;

public class SampleCivilianEvent implements CivilianMessageEvent{

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public SampleCivilianEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(CivilianMessage message) {
        Civilian civilian = this.wp.reflectedMessage(message);
        this.vsp.getVictimSelector().add(civilian);
    }

    @Override
    public void receivedVoice(CivilianMessage message) {
        this.receivedRadio(message);
    }
}
