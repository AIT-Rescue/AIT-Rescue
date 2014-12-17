package comlib.event.topdown;

import comlib.event.MessageEvent;
import comlib.message.topdown.CommandPolice;

public class EventCommandPolice implements MessageEvent<CommandPolice> {

	public void receivedRadio(CommandPolice msg)
	{
		// You cannot write here.
	}

	public void receivedVoice(CommandPolice msg)
	{
		// You cannot write here.
		// This code is default handler.
		this.receivedRadio(msg);
	}
}
