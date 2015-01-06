package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.MessageID;

public class CommandFire extends MessageCommand
{
	/* below id is same to information.MessageFireBrigade */
	public static final int ACTION_REST = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_EXTINGUISH = 2;
	public static final int ACTION_REFILL = 3;

	private int myAction;

	public CommandFire(EntityID toID, EntityID targetID, int action)
	{
		super(MessageID.fireCommand, toID, targetID);
		myAction = action;
	}

	public CommandFire(int time, int ttl, int targetID, int toID, int action)
	{
		super(MessageID.fireCommand, time, ttl, targetID, toID);
		myAction = action;
	}

	public int getAction()
	{ return myAction; }
}
