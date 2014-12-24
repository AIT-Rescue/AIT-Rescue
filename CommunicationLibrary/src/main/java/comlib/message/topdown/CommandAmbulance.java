package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.ActionID;
import comlib.message.MessageID;

public class CommandAmbulance extends MessageCommand
{
	public CommandAmbulance(EntityID toID, EntityID targetID, ActionID action)
	{
		super(MessageID.ambulanceCommand, toID, targetID, action);
	}

	public CommandAmbulance(int time, int ttl, int action, int targetID, int toID)
	{
		super(MessageID.ambulanceCommand, time, ttl, action, targetID, toID);
	}
}
