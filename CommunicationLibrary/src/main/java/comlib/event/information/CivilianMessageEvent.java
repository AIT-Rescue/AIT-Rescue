package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.CivilianMessage;


public interface CivilianMessageEvent extends MessageEvent<CivilianMessage> {
    @Override
	public void receivedRadio(CivilianMessage msg);

    @Override
	public void receivedVoice(CivilianMessage msg);
}
