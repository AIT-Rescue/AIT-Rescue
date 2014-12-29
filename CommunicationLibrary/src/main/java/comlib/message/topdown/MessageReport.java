package comlib.message.topdown;

import comlib.message.CommunicationMessage;
import comlib.message.MessageID;
import rescuecore2.worldmodel.EntityID;

public class MessageReport extends CommunicationMessage
{
	private int rawFromID;
	private EntityID reportFromID;
	private boolean reportDone;

	public MessageReport(boolean isDone)
	{
		super(MessageID.reportMessage);
		reportDone = isDone;
	}

	public MessageReport(int time, int ttl, boolean isDone)
	{
		super(MessageID.reportMessage, time, ttl);
		reportDone = isDone;
	}

	public EntityID getFromID()
	{
		if ( reportFromID == null )
		{ reportFromID = new EntityID(rawFromID); }
		return reportFromID;
	}

	public boolean isDone()
	{ return this.reportDone; }

	public boolean isFailed()
	{ return !this.reportDone; }
}
