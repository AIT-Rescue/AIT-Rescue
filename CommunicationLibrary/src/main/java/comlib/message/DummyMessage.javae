package comlib.message;


public class MessageDummy extends CommunicationMessage
{
	private int dummyTest;

	public MessageDummy(int test)
	{
		super(MessageID.dummyMessage);
		dummyTest = test;
	}

	public MessageDummy(int time, int ttl, int test)
	{
		super(MessageID.dummyMessage, time, ttl);
		dummyTest = test;
	}

	public int getValue() { return this.dummyTest; }
}
