package comlib.message;

import rescuecore2.worldmodel.EntityID;

abstract public class MessageCommand extends CommunicationMessage
{
	protected int rawToID;
	protected int rawTargetID;
	protected EntityID commandToID;
	protected EntityID commandTargetID;

	public MessageCommand(int messageID, EntityID toID, EntityID targetID)
	{
		super(messageID);
		this.commandToID = toID;
		this.commandTargetID = targetID;
	}

	public MessageCommand(int messageID, int time, int ttl, int targetID, int toID)
	{
		super(messageID, time, ttl);
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
}
