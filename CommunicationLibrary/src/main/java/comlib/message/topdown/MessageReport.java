package comlib.message.topdown;

import comlib.message.CommunicationMessage;
import comlib.message.MessageID;

public class MessageReport extends CommunicationMessage
{
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

	public boolean isDone()
	{ return this.reportDone; }

	public boolean isFailed()
	{ return !this.reportDone; }
}
