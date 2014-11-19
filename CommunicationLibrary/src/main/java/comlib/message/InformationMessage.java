package comlib.message;


abstract public class InformationMessage extends CommunicationMessage
{
	public InformationMessage(int messageID)
	{
		super(messageID);
	}

	public InformationMessage(int messageID, int time, int ttl)
	{
		super(messageID, time, ttl);
	}
}
