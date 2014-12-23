package comlib.message;

import rescuecore2.worldmodel.EntityID;
import comlib.message.ActionID;

abstract public class CommandMessage extends CommunicationMessage
{
	protected int rawToID;
	protected int rawTargetID;
	protected int rawAction;
	protected EntityID commandToID;
	protected EntityID commandTargetID;
	protected ActionID commandAction;

	public CommandMessage(int messageID, EntityID toID, EntityID targetID, ActionID action)
	{
		super(messageID);
		this.commandToID = toID;
		this.commandTargetID = targetID;
		this.commandAction = action;
	}

	public CommandMessage(int messageID, int time, int ttl, int action, int targetID, int toID)
	{
		super(messageID, time, ttl);
		this.rawAction = action;
		this.rawTargetID = targetID;
		this.rawToID = toID;
	}
}
