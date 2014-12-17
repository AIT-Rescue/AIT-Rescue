package comlib.event.topdown;

import comlib.event.MessageEvent;
import comlib.message.topdown.CommandScout;

public class EventCommandScout implements MessageEvent<CommandScout> {

	public void receivedRadio(CommandScout msg)
	{
		// You cannot write here.
	}

	public void receivedVoice(CommandScout msg)
	{
		// You cannot write here.
		// This code is default handler.
		this.receivedRadio(msg);
	}
}
