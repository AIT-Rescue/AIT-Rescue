package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.ActionID;
import comlib.message.MessageID;

public class CommandPolice extends MessageCommand
{
	public CommandPolice(EntityID toID, EntityID targetID, ActionID action)
	{
		super(MessageID.policeCommand, toID, targetID, action);
	}

	public CommandPolice(int time, int ttl, int action, int targetID, int toID)
	{
		super(MessageID.policeCommand, time, ttl, action, targetID, toID);
	}
}
