package adk.sample.basic.event;

import adk.team.util.provider.VictimSelectorProvider;
import adk.team.util.provider.WorldProvider;
import comlib.event.information.MessageAmbulanceTeamEvent;
import comlib.manager.MessageReflectHelper;
import comlib.message.information.MessageAmbulanceTeam;
import rescuecore2.standard.entities.AmbulanceTeam;

public class BasicAmbulanceEvent implements MessageAmbulanceTeamEvent {

    private WorldProvider wp;
    private VictimSelectorProvider vsp;

    public BasicAmbulanceEvent(WorldProvider worldProvider, VictimSelectorProvider victimSelectorProvider) {
        this.wp = worldProvider;
        this.vsp = victimSelectorProvider;
    }

    @Override
    public void receivedRadio(MessageAmbulanceTeam message) {
        if(message.getHumanID().getValue() != this.wp.getOwnerID().getValue() ) {
            AmbulanceTeam ambulanceTeam = MessageReflectHelper.reflectedMessage(this.wp.getWorld(), message);
            this.vsp.getVictimSelector().add(ambulanceTeam);
        }
    }

    @Override
    public void receivedVoice(MessageAmbulanceTeam message) {
        this.receivedRadio(message);
    }


}
