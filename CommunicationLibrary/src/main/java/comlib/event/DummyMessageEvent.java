package comlib.event;

import comlib.message.DummyMessage;

public class DummyMessageEvent implements MessageEvent<DummyMessage> {

	public void receivedRadio(DummyMessage msg)
	{
		// You cannot write here.
	}

	public void receivedVoice(DummyMessage msg)
	{
		// You cannot write here.
		// This code is default handler.
		this.receivedRadio(msg);
	}
}
