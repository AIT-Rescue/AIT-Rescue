package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.AmbulanceTeamMessage;


public interface AmbulanceTeamMessageEvent extends MessageEvent<AmbulanceTeamMessage> {
	@Override
    public void receivedRadio(AmbulanceTeamMessage msg);

    @Override
	public void receivedVoice(AmbulanceTeamMessage msg);
}
