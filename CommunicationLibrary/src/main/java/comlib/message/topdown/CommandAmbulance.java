package comlib.message.topdown;

import comlib.message.CommunicationMessage;
import comlib.message.MessageID;

public class CommandAmbulance extends CommunicationMessage
{
	private int dummyTest;

	public CommandAmbulance(int test)
	{
		super(MessageID.ambulanceCommand);
		dummyTest = test;
	}

	public CommandAmbulance(int time, int ttl, int test)
	{
		super(MessageID.ambulanceCommand, time, ttl);
		dummyTest = test;
	}

	public int getValue() { return this.dummyTest; }
}
