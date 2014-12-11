package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessageAmbulanceTeam;


public interface EventMessageAmbulanceTeam extends MessageEvent<MessageAmbulanceTeam> {
	@Override
    public void receivedRadio(MessageAmbulanceTeam msg);

    @Override
	public void receivedVoice(MessageAmbulanceTeam msg);
}
