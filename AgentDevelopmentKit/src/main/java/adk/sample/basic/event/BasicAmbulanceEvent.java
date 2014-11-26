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
    public void receivedRadio(AmbulanceTeamMessage msg) {
        AmbulanceTeam ambulanceTeam = reflectedMessage(this.wp.getWorld(), msg);
        this.vsp.getVictimSelector().add(ambulanceTeam);
    }

    @Override
    public void receivedVoice(AmbulanceTeamMessage msg) {
        this.receivedRadio(msg);
    }

    public AmbulanceTeam reflectedMessage(StandardWorldModel swm, AmbulanceTeamMessage msg) {
        AmbulanceTeam ambulanceteam = (AmbulanceTeam) swm.getEntity(msg.getHumanID());
        if (ambulanceteam == null) {
            swm.addEntity(new AmbulanceTeam(msg.getHumanID()));
            ambulanceteam = (AmbulanceTeam) swm.getEntity(msg.getHumanID());
        }
        ambulanceteam.isHPDefined();
        ambulanceteam.isBuriednessDefined();
        ambulanceteam.isDamageDefined();
        ambulanceteam.isPositionDefined();
        ambulanceteam.setHP(msg.getHP());
        ambulanceteam.setBuriedness(msg.getBuriedness());
        ambulanceteam.setDamage(msg.getDamage());
        ambulanceteam.setPosition(msg.getPosition());

        return ambulanceteam;
    }
}
