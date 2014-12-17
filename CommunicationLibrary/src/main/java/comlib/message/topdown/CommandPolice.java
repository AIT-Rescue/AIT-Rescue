package comlib.message.topdown;

import comlib.message.CommunicationMessage;
import comlib.message.MessageID;

public class CommandPolice extends CommunicationMessage
{
	private int dummyTest;

	public CommandPolice(int test)
	{
		super(MessageID.reportMessage);
		dummyTest = test;
	}

	public CommandPolice(int time, int ttl, int test)
	{
		super(MessageID.reportMessage, time, ttl);
		dummyTest = test;
	}

	public int getValue() { return this.dummyTest; }
}
