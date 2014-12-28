package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.MessageID;

public class CommandScout extends MessageCommand
{
	private int scoutRange;

	public CommandScout(EntityID toID, EntityID targetID, int range)
	{
		super(MessageID.scoutCommand, toID, targetID);
		scoutRange = range;
	}

	public CommandScout(int time, int ttl, int targetID, int toID, int range)
	{
		super(MessageID.scoutCommand, time, ttl, targetID, toID);
		scoutRange = range;
	}

	public int getRange()
	{ return scoutRange; }
}
