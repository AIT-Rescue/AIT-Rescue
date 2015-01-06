package comlib.message.topdown;

import rescuecore2.worldmodel.EntityID;
import comlib.message.MessageCommand;
import comlib.message.MessageID;

public class CommandAmbulance extends MessageCommand
{
	/* below id is same to information.MessageAmbulanceTeam */
	public static final int ACTION_REST = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_RESCUE = 2;
	public static final int ACTION_LOAD = 3;

	private int myAction;

	public CommandAmbulance(EntityID toID, EntityID targetID, int action)
	{
		super(MessageID.ambulanceCommand, toID, targetID);
		myAction = action;
	}

	public CommandAmbulance(int time, int ttl, int targetID, int toID, int action)
	{
		super(MessageID.ambulanceCommand, time, ttl, targetID, toID);
		myAction = action;
	}

	public int getAction()
	{ return myAction; }
}
