package comlib.message;


abstract public class MessageInformation extends CommunicationMessage
{
	public MessageInformation(int messageID)
	{
		super(messageID);
	}

	public MessageInformation(int messageID, int time, int ttl)
	{
		super(messageID, time, ttl);
	}
}
