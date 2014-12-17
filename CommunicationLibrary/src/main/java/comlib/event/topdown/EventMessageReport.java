package comlib.event.topdown;

import comlib.event.MessageEvent;
import comlib.message.topdown.MessageReport;

public class EventMessageReport implements MessageEvent<MessageReport> {

	public void receivedRadio(MessageReport msg)
	{
		// You cannot write here.
	}

	public void receivedVoice(MessageReport msg)
	{
		// You cannot write here.
		// This code is default handler.
		this.receivedRadio(msg);
	}
}
