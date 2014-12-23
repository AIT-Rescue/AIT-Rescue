package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessageAmbulanceTeam;


public interface MessageAmbulanceTeamEvent extends MessageEvent<MessageAmbulanceTeam> {
	@Override
    public void receivedRadio(MessageAmbulanceTeam msg);

    @Override
	public void receivedVoice(MessageAmbulanceTeam msg);
}
