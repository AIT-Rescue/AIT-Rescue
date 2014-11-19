package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.BuildingMessage;


public interface BuildingMessageEvent extends MessageEvent<BuildingMessage> {
    @Override
	public void receivedRadio(BuildingMessage msg);

    @Override
	public void receivedVoice(BuildingMessage msg);
}
