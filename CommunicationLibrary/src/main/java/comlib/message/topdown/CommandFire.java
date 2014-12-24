package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.ActionID;
import comlib.message.MessageID;

public class CommandFire extends MessageCommand
{
	public CommandFire(EntityID toID, EntityID targetID, ActionID action)
	{
		super(MessageID.fireCommand, toID, targetID, action);
	}

	public CommandFire(int time, int ttl, int action, int targetID, int toID)
	{
		super(MessageID.fireCommand, time, ttl, action, targetID, toID);
	}
}
