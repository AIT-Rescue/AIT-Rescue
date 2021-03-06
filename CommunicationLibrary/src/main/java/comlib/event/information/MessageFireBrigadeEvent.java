package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessageFireBrigade;


public interface MessageFireBrigadeEvent extends MessageEvent<MessageFireBrigade> {
	@Override
    public void receivedRadio(MessageFireBrigade msg);

    @Override
	public void receivedVoice(MessageFireBrigade msg);
}
