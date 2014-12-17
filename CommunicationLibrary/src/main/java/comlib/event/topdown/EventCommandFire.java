package comlib.event.topdown;

import comlib.event.MessageEvent;
import comlib.message.topdown.CommandFire;

public class EventCommandFire implements MessageEvent<CommandFire> {

	public void receivedRadio(CommandFire msg)
	{
		// You cannot write here.
	}

	public void receivedVoice(CommandFire msg)
	{
		// You cannot write here.
		// This code is default handler.
		this.receivedRadio(msg);
	}
}
