package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.FireBrigadeMessage;


public interface FireBrigadeMessageEvent extends MessageEvent<FireBrigadeMessage> {
	@Override
    public void receivedRadio(FireBrigadeMessage msg);

    @Override
	public void receivedVoice(FireBrigadeMessage msg);
}
