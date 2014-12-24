package comlib.message;


abstract public class MessageMap extends MessageInformation {
	public MessageMap(int messageID) {
		super(messageID);
	}

	public MessageMap(int messageID, int time, int ttl) {
		super(messageID, time, ttl);
	}
}
