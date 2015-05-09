package sample.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageAmbulanceTeamEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageAmbulanceTeam;
import rescuecore2.standard.entities.AmbulanceTeam;

public class SampleAmbulanceEvent implements MessageAmbulanceTeamEvent {

    private WorldProvider provider;
    private VictimSelectorProvider vsp;

    public SampleAmbulanceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.provider = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageAmbulanceTeam message) {
        if(message.getHumanID().getValue() != this.provider.getOwnerID().getValue() ) {
            AmbulanceTeam ambulanceTeam = MessageReflectHelper.reflectedMessage(this.provider.getWorld(), message);
            this.vsp.getVictimSelector().add(ambulanceTeam);
        }
    }

    @Override
    public void receivedVoice(MessageAmbulanceTeam message) {
        this.receivedRadio(message);
    }


}
