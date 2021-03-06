package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessageCivilian;


public interface MessageCivilianEvent extends MessageEvent<MessageCivilian> {
    @Override
	public void receivedRadio(MessageCivilian msg);

    @Override
	public void receivedVoice(MessageCivilian msg);
}
