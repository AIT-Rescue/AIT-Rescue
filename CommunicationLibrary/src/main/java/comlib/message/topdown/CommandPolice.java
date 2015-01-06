package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.MessageID;

public class CommandPolice extends MessageCommand
{
	/* below id is same to information.MessagePoliceForce */
	public static final int ACTION_REST = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_CLEAR = 2;

	private int myAction;

	public CommandPolice(EntityID toID, EntityID targetID, int action)
	{
		super(MessageID.policeCommand, toID, targetID);
		myAction = action;
	}

	public CommandPolice(int time, int ttl, int targetID, int toID, int action)
	{
		super(MessageID.policeCommand, time, ttl, targetID, toID);
		myAction = action;
	}

	public int getAction()
	{ return myAction; }
}
