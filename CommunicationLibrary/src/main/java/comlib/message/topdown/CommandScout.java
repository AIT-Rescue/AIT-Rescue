package comlib.message.topdown;

import comlib.message.CommunicationMessage;
import comlib.message.MessageID;

public class CommandScout extends CommunicationMessage
{
	private int dummyTest;

	public CommandScout(int test)
	{
		super(MessageID.scoutCommand);
		dummyTest = test;
	}

	public CommandScout(int time, int ttl, int test)
	{
		super(MessageID.scoutCommand, time, ttl);
		dummyTest = test;
	}

	public int getValue() { return this.dummyTest; }
}
