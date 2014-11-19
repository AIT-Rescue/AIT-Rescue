package comlib.message;


abstract public class MapMessage extends InformationMessage {
	public MapMessage(int messageID) {
		super(messageID);
	}

	public MapMessage(int messageID, int time, int ttl) {
		super(messageID, time, ttl);
	}
}
