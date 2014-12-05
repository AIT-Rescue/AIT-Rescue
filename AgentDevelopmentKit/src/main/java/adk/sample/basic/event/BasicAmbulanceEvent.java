package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.AmbulanceTeamMessageEvent;
import comlib.message.information.AmbulanceTeamMessage;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.StandardWorldModel;

public class BasicAmbulanceEvent implements AmbulanceTeamMessageEvent {

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicAmbulanceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(AmbulanceTeamMessage message) {
        AmbulanceTeam ambulanceTeam = this.wp.reflectedMessage(message);
        this.vsp.getVictimSelector().add(ambulanceTeam);
    }

    @Override
    public void receivedVoice(AmbulanceTeamMessage message) {
        this.receivedRadio(message);
    }


}
