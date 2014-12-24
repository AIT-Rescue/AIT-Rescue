package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.ActionID;
import comlib.message.MessageID;

public class CommandScout extends MessageCommand
{
	public CommandScout(EntityID toID, EntityID targetID, ActionID action)
	{
		super(MessageID.scoutCommand, toID, targetID, action);
	}

	public CommandScout(int time, int ttl, int action, int targetID, int toID)
	{
		super(MessageID.scoutCommand, time, ttl, action, targetID, toID);
	}
}
