package comlib.event.topdown;

import comlib.event.MessageEvent;
import comlib.message.topdown.CommandAmbulance;

public class EventCommandAmbulance implements MessageEvent<CommandAmbulance> {

	public void receivedRadio(CommandAmbulance msg)
	{
		// You cannot write here.
	}

	public void receivedVoice(CommandAmbulance msg)
	{
		// You cannot write here.
		// This code is default handler.
		this.receivedRadio(msg);
	}
}
