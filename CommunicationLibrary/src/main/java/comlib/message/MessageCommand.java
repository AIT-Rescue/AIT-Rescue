package comlib.message;

import rescuecore2.worldmodel.EntityID;
import comlib.message.ActionID;

abstract public class MessageCommand extends CommunicationMessage
{
	protected int rawToID;
	protected int rawTargetID;
	protected int rawAction;
	protected EntityID commandToID;
	protected EntityID commandTargetID;
	protected ActionID commandAction;

	public MessageCommand(int messageID, EntityID toID, EntityID targetID, ActionID action)
	{
		super(messageID);
		this.commandToID = toID;
		this.commandTargetID = targetID;
		this.commandAction = action;
	}

	public MessageCommand(int messageID, int time, int ttl, int action, int targetID, int toID)
	{
		super(messageID, time, ttl);
		this.rawAction = action;
		this.rawTargetID = targetID;
		this.rawToID = toID;
	}

	public EntityID getToID()
	{
		if ( commandToID == null )
		{ commandToID = new EntityID(rawToID); }
		return commandToID;
	}

	public EntityID getTargetID()
	{
		if ( commandTargetID == null )
		{ commandTargetID = new EntityID(rawTargetID); }
		return commandTargetID;
	}

	public ActionID getActionID()
	{
		if ( commandAction == null )
		{ commandAction = new ActionID(rawAction); }
		return commandAction;
	}
}
