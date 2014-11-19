package comlib.message;


public class DummyMessage extends CommunicationMessage
{
	private int dummyTest;

	public DummyMessage(int test)
	{
		super(MessageID.dummyMessage);
		dummyTest = test;
	}

	public DummyMessage(int time, int ttl, int test)
	{
		super(MessageID.dummyMessage, time, ttl);
		dummyTest = test;
	}

	public int getValue() { return this.dummyTest; }
}
