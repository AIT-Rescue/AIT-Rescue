package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessageBuilding;


public interface EventMessageBuilding extends MessageEvent<MessageBuilding> {
    @Override
	public void receivedRadio(MessageBuilding msg);

    @Override
	public void receivedVoice(MessageBuilding msg);
}
