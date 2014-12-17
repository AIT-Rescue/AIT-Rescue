package comlib.message.topdown;

import comlib.message.CommunicationMessage;
import comlib.message.MessageID;

public class CommandFire extends CommunicationMessage
{
	private int dummyTest;

	public CommandFire(int test)
	{
		super(MessageID.reportMessage);
		dummyTest = test;
	}

	public CommandFire(int time, int ttl, int test)
	{
		super(MessageID.reportMessage, time, ttl);
		dummyTest = test;
	}

	public int getValue() { return this.dummyTest; }
}
