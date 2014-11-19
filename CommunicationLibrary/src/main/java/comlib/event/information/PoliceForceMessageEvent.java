package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.PoliceForceMessage;


public interface PoliceForceMessageEvent extends MessageEvent<PoliceForceMessage> {
	@Override
    public void receivedRadio(PoliceForceMessage msg);

    @Override
	public void receivedVoice(PoliceForceMessage msg);
}
