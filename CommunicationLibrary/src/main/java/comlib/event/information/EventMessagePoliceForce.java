package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessagePoliceForce;


public interface EventMessagePoliceForce extends MessageEvent<MessagePoliceForce> {
	@Override
    public void receivedRadio(MessagePoliceForce msg);

    @Override
	public void receivedVoice(MessagePoliceForce msg);
}
